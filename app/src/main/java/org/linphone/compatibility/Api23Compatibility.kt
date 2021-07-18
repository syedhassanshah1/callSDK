
package org.linphone.compatibility

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings

@TargetApi(23)
class Api23Compatibility {
    companion object {
        fun hasPermission(context: Context, permission: String): Boolean {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }

        fun canDrawOverlay(context: Context): Boolean {
            return Settings.canDrawOverlays(context)
        }
    }
}
