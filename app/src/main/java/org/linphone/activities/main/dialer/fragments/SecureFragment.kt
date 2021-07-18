
package org.linphone.activities.main.dialer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.linphone.activities.GenericFragment
import org.linphone.core.tools.Log

abstract class SecureFragment<T : ViewDataBinding> : GenericFragment<T>() {
    protected var isSecure: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Assume we might want to be secure to prevent quick visible blink while screen recording.
        enableSecureMode(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        if (isSecure) {
            enableSecureMode(isSecure)
        } else {
            // This is a workaround to prevent a small blink showing the previous secured screen
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    delay(200)
                    enableSecureMode(isSecure)
                }
            }
        }
        super.onResume()
    }

    private fun enableSecureMode(enable: Boolean) {
        Log.d("[Secure Fragment] ${if (enable) "Enabling" else "Disabling"} secure flag on window")
        val window = requireActivity().window
        val windowManager = requireActivity().windowManager

        val flags: Int = window.attributes.flags
        if ((enable && flags and WindowManager.LayoutParams.FLAG_SECURE != 0) ||
            (!enable && flags and WindowManager.LayoutParams.FLAG_SECURE == 0)) {
            Log.d("[Secure Fragment] Secure flag is already ${if (enable) "enabled" else "disabled"}, skipping...")
            return
        }

        if (enable) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }

        if (ViewCompat.isAttachedToWindow(window.decorView)) {
            Log.d("[Secure Fragment] Redrawing window decorView to apply flag")
            windowManager.updateViewLayout(window.decorView, window.attributes)
        }
    }
}
