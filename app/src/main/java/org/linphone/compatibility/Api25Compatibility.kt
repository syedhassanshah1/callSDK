
package org.linphone.compatibility

import android.annotation.TargetApi
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Build
import android.provider.Settings
import org.linphone.utils.ShortcutsHelper

@TargetApi(25)
class Api25Compatibility {
    companion object {
        fun getDeviceName(context: Context): String {
            var name = Settings.Global.getString(
                context.contentResolver, Settings.Global.DEVICE_NAME
            )
            if (name == null) {
                name = BluetoothAdapter.getDefaultAdapter().name
            }
            if (name == null) {
                name = Settings.Secure.getString(
                    context.contentResolver,
                    "bluetooth_name"
                )
            }
            if (name == null) {
                name = Build.MANUFACTURER + " " + Build.MODEL
            }
            return name
        }

        fun createShortcutsToContacts(context: Context) {
            ShortcutsHelper.createShortcutsToContacts(context)
        }

        fun createShortcutsToChatRooms(context: Context) {
            ShortcutsHelper.createShortcutsToChatRooms(context)
        }

        fun removeShortcuts(context: Context) {
            ShortcutsHelper.removeShortcuts(context)
        }
    }
}
