
package org.linphone.views

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * The purpose of this class is to have a TextView automatically configured for marquee ellipsize.
 */
class MarqueeTextView : AppCompatTextView {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    fun init() {
        ellipsize = TextUtils.TruncateAt.MARQUEE
        marqueeRepeatLimit = -0x1
        isSelected = true
    }
}
