
package org.linphone.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.linphone.BaseApplication.Companion.ensureCoreExists
import org.linphone.core.tools.Log

class CorePushReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        ensureCoreExists(context.applicationContext, true)
        Log.i("[Push Receiver] Push notification received")
    }
}
