
package org.linphone.compatibility

import android.annotation.TargetApi
import android.app.*
import android.content.Context

@TargetApi(27)
class Api27Compatibility {
    companion object {
        fun setShowWhenLocked(activity: Activity, enable: Boolean) {
            activity.setShowWhenLocked(enable)
        }

        fun setTurnScreenOn(activity: Activity, enable: Boolean) {
            activity.setTurnScreenOn(enable)
        }

        fun requestDismissKeyguard(activity: Activity) {
            val keyguardManager = activity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(activity, null)
        }
    }
}
