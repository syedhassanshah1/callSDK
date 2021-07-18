package org.linphone.activities.launcher

import android.os.Bundle
import android.widget.Button
import com.callsdk.builder.CallSDKBuilder
import org.linphone.R
import org.linphone.activities.GenericActivity

class TestActivity : GenericActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        findViewById<Button>(R.id.btnCall).setOnClickListener {
            val sdkOptions = CallSDKBuilder.Builder("uC7SBEVDAqbnVUUfnW3d8DKcPCkim8pS", "Finance", "Hello World").build()
            sdkOptions.initCall(this)
        }
    }
}
