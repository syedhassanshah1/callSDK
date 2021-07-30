package org.linphone.activities.launcher

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.callsdk.builder.CallSDKBuilder
import org.linphone.BaseApplication.Companion.coreContext
import org.linphone.BaseApplication.Companion.corePreferences
import org.linphone.R
import org.linphone.activities.GenericActivity
import org.linphone.activities.launcher.dataModels.CallParametersModel
import org.linphone.activities.main.viewmodels.SharedMainViewModel
import org.linphone.core.*
import org.linphone.core.tools.Log

class LauncherActivity : GenericActivity() {
    private val callSDKBuilder: CallSDKBuilder? by lazy {
        intent.getSerializableExtra("callParams") as? CallSDKBuilder?
    }
    val waitForServerAnswer = MutableLiveData<Boolean>()
    private lateinit var sharedViewModel: SharedMainViewModel
    private var callParams: CallParametersModel? = null
    private val coreListener = object : CoreListenerStub() {
        override fun onRegistrationStateChanged(
                core: Core,
                cfg: ProxyConfig,
                state: RegistrationState,
                message: String
        ) {
            if (cfg == proxyConfigToCheck) {
                Log.i("[Assistant] [Generic Login] Registration state is $state: $message")
                waitForServerAnswer.value = false
                if (state == RegistrationState.Ok) {
                    if (!callParams?.extensionNoTo.isNullOrEmpty()) {
                        coreContext.handler.postDelayed({
                            callParams?.extensionNoTo?.let {
                                coreContext.startCall(it, callParams?.extensionNoFrom ?: "")
                                finishApp()
                            }
                        }, 500)
                        core.removeListener(this)
                    } else {
                        Toast.makeText(this@LauncherActivity, "Unable to find extension", Toast.LENGTH_SHORT).show()
                        releaseExtension()
                    }
                } else if (state == RegistrationState.Failed) {
                    Toast.makeText(this@LauncherActivity, "Unable to connect to server", Toast.LENGTH_SHORT).show()
                    core.removeListener(this)
                    releaseExtension()
                }
            }
        }
    }

    fun releaseExtension() {
        enqueueCall(call = apiService().releaseExtension(hashMapOf(
                "ExtensionNo" to (callParams?.extensionNoFrom ?: ""),
        )),
                listener = { _, _, isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this@LauncherActivity, "Extension is released", Toast.LENGTH_SHORT).show()
                    }
                    finishApp()

                })
    }

    private var proxyConfigToCheck: ProxyConfig? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.launcher_activity)
        sharedViewModel = ViewModelProvider(this).get(SharedMainViewModel::class.java)

    }

    private fun getCallParameters() {
        enqueueCall(call = apiService().getCallParameters(hashMapOf(
                "APIKey" to (callSDKBuilder?.apiKey ?: ""),
                "ChannelName" to (callSDKBuilder?.channelName ?: ""),
                "TempData" to (callSDKBuilder?.extras ?: ""),
        )),
                listener = { response, error, isSuccess ->
                    if (isSuccess) {
                        callParams = response
                        createProxyConfig(sharedViewModel.getAccountCreator(true))
                    } else {
                        Toast.makeText(this, error?.respMessage ?: "Something went wrong", Toast.LENGTH_SHORT).show()
                        finishApp()
                    }
                })
    }

    private fun createProxyConfig(accountCreator: AccountCreator) {
        if (callParams != null) {
            waitForServerAnswer.value = true
            coreContext.core.addListener(coreListener)

            accountCreator.username = callParams?.authorizationUsername
            accountCreator.password = callParams?.authorizationPassword
//            accountCreator.domain = "${callParams?.sIPServerURL}:${callParams?.sIPServerPort}"
            accountCreator.domain = "${callParams?.sIPServerURL}:8061"
            accountCreator.displayName = "test"
            accountCreator.transport = TransportType.Tcp

            val proxyConfig: ProxyConfig? = accountCreator.createProxyConfig()
            proxyConfigToCheck = proxyConfig

            if (proxyConfig == null) {
                Log.e("[Assistant] [Generic Login] Account creator couldn't create proxy config")
                coreContext.core.removeListener(coreListener)
                Toast.makeText(this, "Unable to connect to server", Toast.LENGTH_SHORT).show()
                releaseExtension()
                return
            }
        } else {
            Toast.makeText(this, "Unable to get Extension from server", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        coreContext.handler.postDelayed({ onReady() }, 500)
    }

    private fun onReady() {
        Log.i("[Launcher] Core is ready")

        if (corePreferences.preventInterfaceFromShowingUp) {
            Log.w("[Context] We were asked to not show the user interface")
            finishApp()
            return
        }

        if (corePreferences.enableAnimations) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        enqueueCall(call = apiService().authenticateAPIKey(hashMapOf("APIKey" to (callSDKBuilder?.apiKey ?: ""))),
                listener = { _, _, isSuccess ->
                    if (isSuccess) {
                        getCallParameters()
                    } else {
                        Toast.makeText(this, "API key is invalid.", Toast.LENGTH_SHORT).show()
                        finishApp()
                    }
                })
    }

    private fun finishApp() {
        finish()
    }
}
