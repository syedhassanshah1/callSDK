package org.linphone.activities.call.fragments

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.app.Dialog
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.os.PowerManager
import android.os.SystemClock
import android.view.View
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.google.android.flexbox.FlexboxLayout
import org.linphone.BaseApplication.Companion.coreContext
import org.linphone.BaseApplication.Companion.corePreferences
import org.linphone.R
import org.linphone.activities.GenericFragment
import org.linphone.activities.call.CallActivity
import org.linphone.activities.call.viewmodels.CallsViewModel
import org.linphone.activities.call.viewmodels.ConferenceViewModel
import org.linphone.activities.call.viewmodels.ControlsViewModel
import org.linphone.activities.call.viewmodels.SharedCallViewModel
import org.linphone.activities.main.viewmodels.DialogViewModel
import org.linphone.core.Call
import org.linphone.core.tools.Log
import org.linphone.databinding.CallControlsFragmentBinding
import org.linphone.mediastream.Version
import org.linphone.utils.AppUtils
import org.linphone.utils.DialogUtils
import org.linphone.utils.Event
import org.linphone.utils.PermissionHelper

class ControlsFragment : GenericFragment<CallControlsFragmentBinding>() {
    private lateinit var callsViewModel: CallsViewModel
    private lateinit var controlsViewModel: ControlsViewModel
    private lateinit var conferenceViewModel: ConferenceViewModel
    private lateinit var sharedViewModel: SharedCallViewModel

    private var dialog: Dialog? = null

    override fun getLayoutId(): Int = R.layout.call_controls_fragment

    // We have to use late init here because we need to compute the screen width first
    private lateinit var numpadAnimator: ValueAnimator
    private val proximitySensor by lazy {
        val powerManager = requireActivity().getSystemService<PowerManager>() ?: return@lazy null
        if (powerManager.isWakeLockLevelSupported(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK)) {
            powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, CallActivity::class.java.simpleName).also {
                it.setReferenceCounted(false)
            }
        } else null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this

        sharedViewModel = requireActivity().run {
            ViewModelProvider(this).get(SharedCallViewModel::class.java)
        }
        proximitySensor?.acquire(10 * 60 * 1000L /*10 minutes*/)
        callsViewModel = ViewModelProvider(this).get(CallsViewModel::class.java)
        binding.viewModel = callsViewModel

        controlsViewModel = ViewModelProvider(this).get(ControlsViewModel::class.java)
        binding.controlsViewModel = controlsViewModel

        conferenceViewModel = ViewModelProvider(this).get(ConferenceViewModel::class.java)
        binding.conferenceViewModel = conferenceViewModel
        callsViewModel.callEndedEvent.observe(viewLifecycleOwner, {
            it.consume {
                proximitySensor?.release()
            }
        })
        callsViewModel.currentCallViewModel.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.activeCallTimer.base =
                        SystemClock.elapsedRealtime() - (1000 * it.call.duration) // Linphone timestamps are in seconds
                binding.activeCallTimer.start()
            }
        })

        callsViewModel.noMoreCallEvent.observe(viewLifecycleOwner, {
            it.consume {
                requireActivity().finish()
            }
        })

        callsViewModel.askWriteExternalStoragePermissionEvent.observe(viewLifecycleOwner, {
            it.consume {
                if (!PermissionHelper.get().hasWriteExternalStorage()) {
                    Log.i("[Controls Fragment] Asking for WRITE_EXTERNAL_STORAGE permission")
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                }
            }
        })

        callsViewModel.callUpdateEvent.observe(viewLifecycleOwner, {
            it.consume { call ->
                if (call.state == Call.State.StreamsRunning) {
                    dialog?.dismiss()
                } else if (call.state == Call.State.UpdatedByRemote) {
                    if (call.currentParams.videoEnabled() != call.remoteParams?.videoEnabled()) {
                        showCallVideoUpdateDialog(call)
                    }
                }
            }
        })

        controlsViewModel.askPermissionEvent.observe(viewLifecycleOwner, {
            it.consume { permission ->
                Log.i("[Controls Fragment] Asking for $permission permission")
                requestPermissions(arrayOf(permission), 0)
            }
        })

        controlsViewModel.toggleNumpadEvent.observe(viewLifecycleOwner, {
            it.consume { open ->
                if (this::numpadAnimator.isInitialized) {
                    if (open) {
                        numpadAnimator.start()
                    } else {
                        numpadAnimator.reverse()
                    }
                }
            }
        })

        controlsViewModel.somethingClickedEvent.observe(viewLifecycleOwner, {
            it.consume {
                sharedViewModel.resetHiddenInterfaceTimerInVideoCallEvent.value = Event(true)
            }
        })

        if (Version.sdkAboveOrEqual(Version.API23_MARSHMALLOW_60)) {
            checkPermissions()
        }
    }

    override fun onStart() {
        super.onStart()

        initNumpadLayout()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == 0) {
            for (i in permissions.indices) {
                when (permissions[i]) {
                    Manifest.permission.RECORD_AUDIO -> if (grantResults[i] == PERMISSION_GRANTED) {
                        Log.i("[Controls Fragment] RECORD_AUDIO permission has been granted")
                        controlsViewModel.updateMuteMicState()
                    }
                    Manifest.permission.CAMERA -> if (grantResults[i] == PERMISSION_GRANTED) {
                        Log.i("[Controls Fragment] CAMERA permission has been granted")
                        coreContext.core.reloadVideoDevices()
                    }
                }
            }
        } else if (requestCode == 1 && grantResults[0] == PERMISSION_GRANTED) {
            callsViewModel.takeScreenshot()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @TargetApi(Version.API23_MARSHMALLOW_60)
    private fun checkPermissions() {
        val permissionsRequiredList = arrayListOf<String>()

        if (!PermissionHelper.get().hasRecordAudioPermission()) {
            Log.i("[Controls Fragment] Asking for RECORD_AUDIO permission")
            permissionsRequiredList.add(Manifest.permission.RECORD_AUDIO)
        }

        if (coreContext.isVideoCallOrConferenceActive() && !PermissionHelper.get().hasCameraPermission()) {
            Log.i("[Controls Fragment] Asking for CAMERA permission")
            permissionsRequiredList.add(Manifest.permission.CAMERA)
        }

        if (permissionsRequiredList.isNotEmpty()) {
            val permissionsRequired = arrayOfNulls<String>(permissionsRequiredList.size)
            permissionsRequiredList.toArray(permissionsRequired)
            requestPermissions(permissionsRequired, 0)
        }
    }

    private fun showCallVideoUpdateDialog(call: Call) {
        val viewModel = DialogViewModel(AppUtils.getString(R.string.call_video_update_requested_dialog))
        dialog = DialogUtils.getDialog(requireContext(), viewModel)

        viewModel.showCancelButton({
            callsViewModel.answerCallVideoUpdateRequest(call, false)
            dialog?.dismiss()
        }, getString(R.string.dialog_decline))

        viewModel.showOkButton({
            callsViewModel.answerCallVideoUpdateRequest(call, true)
            dialog?.dismiss()
        }, getString(R.string.dialog_accept))

        dialog?.show()
    }

    private fun initNumpadLayout() {
        val screenWidth = coreContext.screenWidth
        numpadAnimator = ValueAnimator.ofFloat(screenWidth, 0f).apply {
            addUpdateListener {
                val value = it.animatedValue as Float
                view?.findViewById<FlexboxLayout>(R.id.numpad)?.translationX = -value
                duration = if (corePreferences.enableAnimations) 500 else 0
            }
        }
        // Hide the numpad here as we can't set the translationX property on include tag in layout
        if (controlsViewModel.numpadVisibility.value == false) {
            view?.findViewById<FlexboxLayout>(R.id.numpad)?.translationX = -screenWidth
        }
    }
}
