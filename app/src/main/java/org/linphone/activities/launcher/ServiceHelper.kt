package org.linphone.activities.launcher

import com.google.gson.Gson
import org.linphone.activities.launcher.dataModels.CallParametersModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceHelper<RESULT>(
        val listener: (response: RESULT?, error: CallParametersModel?, isSuccess: Boolean) -> Unit
) {

    fun enqueueCall(call: Call<RESULT>, tag: String) {
        call.enqueue(object : Callback<RESULT> {
            override fun onResponse(call: Call<RESULT>, response: Response<RESULT>) {
                if (response.isSuccessful) {
                    listener.invoke(response.body(), null, response.isSuccessful)
                } else {
                    try {
                        val responseWrapper = Gson().fromJson(
                                response.errorBody()?.string(),
                                CallParametersModel::class.java
                        )
                        listener.invoke(null, responseWrapper, response.isSuccessful)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<RESULT>, t: Throwable) {
                listener.invoke(null, null, false)
                t.printStackTrace()
            }
        })
    }
}
