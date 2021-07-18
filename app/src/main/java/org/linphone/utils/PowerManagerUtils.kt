
package org.linphone.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri

class PowerManagerUtils {
    companion object {
        // https://stackoverflow.com/questions/31638986/protected-apps-setting-on-huawei-phones-and-how-to-handle-it
        private val POWER_MANAGER_INTENTS = arrayOf(
            Intent()
                .setComponent(
                    ComponentName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.letv.android.letvsafe",
                        "com.letv.android.letvsafe.AutobootManageActivity"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.systemmanager.optimize.process.ProtectActivity"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.coloros.safecenter",
                        "com.coloros.safecenter.startupapp.StartupAppListActivity"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.oppo.safe",
                        "com.oppo.safe.permission.startup.StartupAppListActivity"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.iqoo.secure",
                        "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.iqoo.secure",
                        "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.samsung.android.lool",
                        "com.samsung.android.sm.ui.battery.BatteryActivity"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.htc.pitroad",
                        "com.htc.pitroad.landingpage.activity.LandingPageActivity"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.asus.mobilemanager",
                        "com.asus.mobilemanager.autostart.AutoStartActivity"
                    )
                ),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.asus.mobilemanager",
                        "com.asus.mobilemanager.entry.FunctionActivity"
                    )
                )
                .setData(Uri.parse("mobilemanager://function/entry/AutoStart")),
            Intent()
                .setComponent(
                    ComponentName(
                        "com.dewav.dwappmanager",
                        "com.dewav.dwappmanager.memory.SmartClearupWhiteList"
                    )
                )
        )

        fun getDevicePowerManagerIntent(context: Context): Intent? {
            for (intent in POWER_MANAGER_INTENTS) {
                if (isIntentCallable(context, intent)) {
                    return intent
                }
            }
            return null
        }

        private fun isIntentCallable(context: Context, intent: Intent): Boolean {
            val list: List<ResolveInfo> = context.packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            return list.isNotEmpty()
        }
    }
}
