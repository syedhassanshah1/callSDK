
package org.linphone

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import org.linphone.core.CoreContext
import org.linphone.core.CorePreferences
import org.linphone.core.Factory
import org.linphone.core.LogCollectionState
import org.linphone.core.tools.Log

class BaseApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var corePreferences: CorePreferences
        lateinit var coreContext: CoreContext

        fun ensureCoreExists(context: Context, pushReceived: Boolean = false) {
            if (::coreContext.isInitialized && !coreContext.stopped) {
                Log.d("[Application] Skipping Core creation (push received? $pushReceived)")
                return
            }

            Factory.instance().setLogCollectionPath(context.filesDir.absolutePath)
            Factory.instance().enableLogCollection(LogCollectionState.Enabled)

            corePreferences = CorePreferences(context)
            corePreferences.copyAssetsFromPackage()

            if (corePreferences.vfsEnabled) {
                CoreContext.activateVFS()
            }

            val config = Factory.instance().createConfigWithFactory(corePreferences.configPath, corePreferences.factoryConfigPath)
            corePreferences.config = config

            val appName = context.getString(R.string.app_name)
            Factory.instance().setDebugMode(corePreferences.debugLogs, appName)

            Log.i("[Application] Core context created ${if (pushReceived) "from push" else ""}")
            coreContext = CoreContext(context, config)
            coreContext.start()
        }
    }

    override fun onCreate() {
        super.onCreate()
        val appName = getString(R.string.app_name)
        android.util.Log.i("[$appName]", "Application is being created")
        ensureCoreExists(applicationContext)
        Log.i("[Application] Created")
    }
}
