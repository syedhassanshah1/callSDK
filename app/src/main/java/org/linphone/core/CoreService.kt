
package org.linphone.core

import android.content.Intent
import org.linphone.BaseApplication.Companion.coreContext
import org.linphone.BaseApplication.Companion.corePreferences
import org.linphone.core.tools.Log
import org.linphone.core.tools.service.CoreService

class CoreService : CoreService() {
    override fun onCreate() {
        super.onCreate()

        coreContext.notificationsManager.service = this
        Log.i("[Service] Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.extras?.get("StartForeground") == true) {
            Log.i("[Service] Starting as foreground due to device boot or app update")
//            coreContext.notificationsManager.startForeground(this, true)
            coreContext.checkIfForegroundServiceNotificationCanBeRemovedAfterDelay(5000)
        } else if (corePreferences.keepServiceAlive) {
            Log.i("[Service] Starting as foreground to keep app alive in background")
//            coreContext.notificationsManager.startForeground(this, false)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun createServiceNotificationChannel() {
        // Done elsewhere
    }

    override fun showForegroundServiceNotification() {
        Log.i("[Service] Starting service as foreground")
        coreContext.notificationsManager.startCallForeground(this)
    }

    override fun hideForegroundServiceNotification() {
        Log.i("[Service] Stopping service as foreground")
        coreContext.notificationsManager.stopCallForeground()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (!corePreferences.keepServiceAlive) {
            if (coreContext.core.isInBackground) {
                Log.i("[Service] Task removed, stopping Core")
                coreContext.stop()
            } else {
                Log.w("[Service] Task removed but Core in not in background, skipping")
            }
        }

        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        Log.i("[Service] Stopping")
        coreContext.notificationsManager.service = null

        super.onDestroy()
    }
}
