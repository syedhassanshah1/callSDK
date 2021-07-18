package org.linphone.activities.launcher.retrofit

import org.linphone.activities.launcher.dataModels.CallParametersModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WebService {

    @POST(WebServiceConstants.GET_CALL_PARAMETERS)
    fun getCallParameters(@Body body: HashMap<String, String>): Call<CallParametersModel?>

    @POST(WebServiceConstants.RELEASE_EXTENSION)
    fun releaseExtension(@Body body: HashMap<String, String>): Call<String?>

    @POST(WebServiceConstants.AUTHENTICATE_API)
    fun authenticateAPIKey(@Body() body: HashMap<String, String>): Call<String?>
}
