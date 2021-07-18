
package org.linphone.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import org.linphone.BaseApplication.Companion.corePreferences
import org.linphone.core.tools.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED, ignoreCase = true)) {
            val autoStart = corePreferences.autoStart
            Log.i("[Boot Receiver] Device is starting, autoStart is $autoStart")
            if (autoStart) {
                val serviceIntent = Intent(Intent.ACTION_MAIN).setClass(context, CoreService::class.java)
                serviceIntent.putExtra("StartForeground", true)
                ContextCompat.startForegroundService(context, serviceIntent)
            }
        } else if (intent.action.equals(Intent.ACTION_MY_PACKAGE_REPLACED, ignoreCase = true)) {
            val autoStart = corePreferences.autoStart
            Log.i("[Boot Receiver] App has been updated, autoStart is $autoStart")
            if (autoStart) {
                val serviceIntent = Intent(Intent.ACTION_MAIN).setClass(context, CoreService::class.java)
                serviceIntent.putExtra("StartForeground", true)
                ContextCompat.startForegroundService(context, serviceIntent)
            }
        }
    }
}
