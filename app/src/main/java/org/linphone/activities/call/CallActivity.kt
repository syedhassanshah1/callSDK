
package org.linphone.activities.call

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.PowerManager
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*
import org.linphone.BaseApplication.Companion.coreContext
import org.linphone.BaseApplication.Companion.corePreferences
import org.linphone.R
import org.linphone.activities.call.viewmodels.ControlsFadingViewModel
import org.linphone.activities.call.viewmodels.SharedCallViewModel
import org.linphone.compatibility.Compatibility
import org.linphone.core.tools.Log
import org.linphone.databinding.CallActivityBinding

class CallActivity : ProximitySensorActivity() {
    private lateinit var binding: CallActivityBinding
    private lateinit var viewModel: ControlsFadingViewModel
    private lateinit var sharedViewModel: SharedCallViewModel

    private var previewX: Float = 0f
    private var previewY: Float = 0f
    private lateinit var videoZoomHelper: VideoZoomHelper
    private val proximitySensor by lazy {
        val powerManager = getSystemService<PowerManager>() ?: return@lazy null
        if (powerManager.isWakeLockLevelSupported(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK)) {
            powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, CallActivity::class.java.simpleName).also {
                it.setReferenceCounted(false)
            }
        } else null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Compatibility.setShowWhenLocked(this, true)
        Compatibility.setTurnScreenOn(this, true)

        binding = DataBindingUtil.setContentView(this, R.layout.call_activity)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(ControlsFadingViewModel::class.java)
        binding.viewModel = viewModel

        sharedViewModel = ViewModelProvider(this).get(SharedCallViewModel::class.java)

        sharedViewModel.toggleDrawerEvent.observe(this, {
            it.consume {
                if (binding.statsMenu.isDrawerOpen(Gravity.LEFT)) {
                    binding.statsMenu.closeDrawer(binding.sideMenuContent, true)
                } else {
                    binding.statsMenu.openDrawer(binding.sideMenuContent, true)
                }
            }
        })

        sharedViewModel.resetHiddenInterfaceTimerInVideoCallEvent.observe(this, {
            it.consume {
                viewModel.showMomentarily()
            }
        })

        coreContext.core.nativeVideoWindowId = binding.remoteVideoSurface
        coreContext.core.nativePreviewWindowId = binding.localPreviewVideoSurface

        binding.setPreviewTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    previewX = v.x - event.rawX
                    previewY = v.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    v.animate()
                        .x(event.rawX + previewX)
                        .y(event.rawY + previewY)
                        .setDuration(0)
                        .start()
                }
                else -> {
                    v.performClick()
                    false
                }
            }
            true
        }

        videoZoomHelper = VideoZoomHelper(this, binding.remoteVideoSurface)

        viewModel.proximitySensorEnabled.observe(this, {
            enableProximitySensor(it)
        })
    }

    override fun onResume() {
        super.onResume()

        if (coreContext.core.callsNb == 0) {
            Log.w("[Call Activity] Resuming but no call found...")
            finish()
        } else {
            coreContext.removeCallOverlay()
        }

        if (corePreferences.fullScreenCallUI) {
            hideSystemUI()
            window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    GlobalScope.launch {
                        delay(2000)
                        withContext(Dispatchers.Main) {
                            hideSystemUI()
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        val core = coreContext.core
        if (core.callsNb > 0) {
            coreContext.createCallOverlay()
        }

        super.onPause()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()

        if (coreContext.isVideoCallOrConferenceActive()) {
            Compatibility.enterPipMode(this)
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        if (isInPictureInPictureMode) {
            viewModel.areControlsHidden.value = true
        }

        if (corePreferences.hideCameraPreviewInPipMode) {
            viewModel.isVideoPreviewHidden.value = isInPictureInPictureMode
        } else {
            viewModel.isVideoPreviewResizedForPip.value = isInPictureInPictureMode
        }
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        if (corePreferences.fullScreenCallUI) {
            theme.applyStyle(R.style.FullScreenTheme, true)
        }
        return theme
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}
