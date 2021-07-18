package org.linphone.activities.launcher

import android.app.Activity
import org.linphone.activities.GenericActivity
import org.linphone.activities.launcher.dataModels.CallParametersModel
import org.linphone.core.CoreContext
import retrofit2.Call

fun Activity.apiService() = (this as GenericActivity).webService

fun <RESULT> Activity.enqueueCall(
    call: Call<RESULT?>,
    listener: (response: RESULT?, error: CallParametersModel?, isSuccess: Boolean) -> Unit
) {
    ServiceHelper(listener).enqueueCall(call, "")
}
fun <RESULT> CoreContext.enqueueCall(
    call: Call<RESULT?>,
    listener: (response: RESULT?, error: CallParametersModel?, isSuccess: Boolean) -> Unit
) {
    ServiceHelper(listener).enqueueCall(call, "")
}
