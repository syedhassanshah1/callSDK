
package org.linphone.utils

import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class LinphoneGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // Tells Glide to only log errors, prevents warnings because contact doesn't have a picture
        builder.setLogLevel(Log.ERROR)
    }
}
