
package org.linphone.utils

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import org.linphone.core.DialPlan
import org.linphone.core.Factory
import org.linphone.core.tools.Log

class PhoneNumberUtils {
    companion object {
        fun getDialPlanForCurrentCountry(context: Context): DialPlan? {
            try {
                val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val countryIso = tm.networkCountryIso
                return getDialPlanFromCountryCode(countryIso)
            } catch (e: java.lang.Exception) {
                Log.e("[Phone Number Utils] $e")
            }
            return null
        }

        @SuppressLint("MissingPermission", "HardwareIds")
        fun getDevicePhoneNumber(context: Context): String? {
            if (PermissionHelper.get().hasReadPhoneState()) {
                try {
                    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    return tm.line1Number
                } catch (e: java.lang.Exception) {
                    Log.e("[Phone Number Utils] $e")
                }
            }
            return null
        }

        fun getDialPlanFromCountryCallingPrefix(countryCode: String): DialPlan? {
            for (c in Factory.instance().dialPlans) {
                if (countryCode == c.countryCallingCode) return c
            }
            return null
        }

        private fun getDialPlanFromCountryCode(countryCode: String): DialPlan? {
            for (c in Factory.instance().dialPlans) {
                if (countryCode.equals(c.isoCountryCode, ignoreCase = true)) return c
            }
            return null
        }
    }
}
