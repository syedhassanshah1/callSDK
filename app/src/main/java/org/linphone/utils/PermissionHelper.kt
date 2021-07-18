
package org.linphone.utils

import android.Manifest
import android.content.Context
import org.linphone.compatibility.Compatibility
import org.linphone.core.tools.Log

/**
 * Helper methods to check whether a permission has been granted and log the result
 */
class PermissionHelper private constructor(private val context: Context) {
    companion object : SingletonHolder<PermissionHelper, Context>(::PermissionHelper)

    private fun hasPermission(permission: String): Boolean {
        val granted = Compatibility.hasPermission(context, permission)

        if (granted) {
            Log.d("[Permission Helper] Permission $permission is granted")
        } else {
            Log.w("[Permission Helper] Permission $permission is denied")
        }

        return granted
    }

    fun hasReadContactsPermission(): Boolean {
        return hasPermission(Manifest.permission.READ_CONTACTS)
    }

    fun hasWriteContactsPermission(): Boolean {
        return hasPermission(Manifest.permission.WRITE_CONTACTS)
    }

    fun hasReadPhoneState(): Boolean {
        return hasPermission(Manifest.permission.READ_PHONE_STATE)
    }

    fun hasReadExternalStorage(): Boolean {
        return hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun hasWriteExternalStorage(): Boolean {
        return hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    fun hasCameraPermission(): Boolean {
        return hasPermission(Manifest.permission.CAMERA)
    }

    fun hasRecordAudioPermission(): Boolean {
        return hasPermission(Manifest.permission.RECORD_AUDIO)
    }
}
