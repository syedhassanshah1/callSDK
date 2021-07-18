package org.linphone.activities.launcher.dataModels
import com.google.gson.annotations.SerializedName

data class CallParametersModel(
    var authorizationPassword: String? = "",
    @SerializedName("AuthorizationUsername")
    var authorizationUsername: String? = "",
    @SerializedName("ExtensionNoFrom")
    var extensionNoFrom: String? = "",
    @SerializedName("ExtensionNoTo")
    var extensionNoTo: String? = "",
    @SerializedName("Is_DTMF")
    var isDTMF: Int? = 0,
    @SerializedName("Is_Hold")
    var isHold: Int? = 0,
    @SerializedName("Is_Mute")
    var isMute: Int? = 0,
    @SerializedName("RespCode")
    var respCode: Int? = 0,
    @SerializedName("RespMessage")
    var respMessage: String? = "",
    @SerializedName("SIPServerPort")
    var sIPServerPort: String? = "",
    @SerializedName("SIPServerURL")
    var sIPServerURL: String? = "",
    @SerializedName("TempDataId")
    var tempDataId: Int? = 0,
    @SerializedName("WebSocketServerURL")
    var webSocketServerURL: String? = ""
)
