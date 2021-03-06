
package org.linphone.activities.call.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import java.util.*
import org.linphone.R
import org.linphone.activities.GenericFragment
import org.linphone.activities.call.viewmodels.SharedCallViewModel
import org.linphone.activities.call.viewmodels.StatusViewModel
import org.linphone.activities.main.viewmodels.DialogViewModel
import org.linphone.core.Call
import org.linphone.core.tools.Log
import org.linphone.databinding.CallStatusFragmentBinding
import org.linphone.utils.DialogUtils
import org.linphone.utils.Event

class StatusFragment : GenericFragment<CallStatusFragmentBinding>() {
    private lateinit var viewModel: StatusViewModel
    private lateinit var sharedViewModel: SharedCallViewModel
    private var zrtpDialog: Dialog? = null

    override fun getLayoutId(): Int = R.layout.call_status_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(StatusViewModel::class.java)
        binding.viewModel = viewModel

        sharedViewModel = requireActivity().run {
            ViewModelProvider(this).get(SharedCallViewModel::class.java)
        }

        binding.setStatsClickListener {
            sharedViewModel.toggleDrawerEvent.value = Event(true)
        }

        binding.setRefreshClickListener {
            viewModel.refreshRegister()
        }

        viewModel.showZrtpDialogEvent.observe(viewLifecycleOwner, {
            it.consume { call ->
                if (call.state == Call.State.Connected || call.state == Call.State.StreamsRunning) {
                    showZrtpDialog(call)
                }
            }
        })
    }

    override fun onDestroy() {
        if (zrtpDialog != null) {
            zrtpDialog?.dismiss()
        }
        super.onDestroy()
    }

    private fun showZrtpDialog(call: Call) {
        if (zrtpDialog != null && zrtpDialog?.isShowing == true) {
            Log.e("[Status Fragment] ZRTP dialog already visible")
            return
        }

        val token = call.authenticationToken
        if (token == null || token.length < 4) {
            Log.e("[Status Fragment] ZRTP token is invalid: $token")
            return
        }

        val toRead: String
        val toListen: String
        when (call.dir) {
            Call.Dir.Incoming -> {
                toRead = token.substring(0, 2)
                toListen = token.substring(2)
            }
            else -> {
                toRead = token.substring(2)
                toListen = token.substring(0, 2)
            }
        }

        val viewModel = DialogViewModel(getString(R.string.zrtp_dialog_message), getString(R.string.zrtp_dialog_title))
        viewModel.showZrtp = true
        viewModel.zrtpReadSas = toRead.toUpperCase(Locale.getDefault())
        viewModel.zrtpListenSas = toListen.toUpperCase(Locale.getDefault())
        viewModel.showIcon = true
        viewModel.iconResource = R.drawable.security_2_indicator

        val dialog: Dialog = DialogUtils.getDialog(requireContext(), viewModel)

        viewModel.showDeleteButton({
            call.authenticationTokenVerified = false
            this@StatusFragment.viewModel.updateEncryptionInfo(call)
            dialog.dismiss()
            zrtpDialog = null
        }, getString(R.string.zrtp_dialog_deny_button_label))

        viewModel.showOkButton({
            call.authenticationTokenVerified = true
            this@StatusFragment.viewModel.updateEncryptionInfo(call)
            dialog.dismiss()
            zrtpDialog = null
        }, getString(R.string.zrtp_dialog_ok_button_label))

        zrtpDialog = dialog
        dialog.show()
    }
}
