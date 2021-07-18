package com.callsdk.builder

import android.content.Context
import android.content.Intent
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import org.linphone.activities.launcher.LauncherActivity

class CallSDKBuilder private constructor(
    @SerializedName("apiKey")
    var apiKey: String?,
    @SerializedName("channelName")
    var channelName: String?,
    @SerializedName("extras")
    var extras: String?
) : Serializable {

    fun initCall(context: Context) {
        val intent: Intent = Intent(context, LauncherActivity::class.java)
        intent.putExtra("callParams", this as Serializable)
        context.startActivity(intent)
    }

    data class Builder(
        private var apiKey: String = "",
        private var channelName: String = "",
        private var extras: String = ""
    ) {

        fun apiKey(APIKey: String) = apply { this.apiKey = APIKey }
        fun channelName(channelName: String) = apply { this.channelName = channelName }
        fun extras(extras: String) = apply { this.extras = extras }
        fun build() = CallSDKBuilder(apiKey, channelName, extras)
    }
}
