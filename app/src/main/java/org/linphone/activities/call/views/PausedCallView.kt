
package org.linphone.activities.call.views

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import org.linphone.R
import org.linphone.activities.call.viewmodels.CallViewModel
import org.linphone.databinding.CallPausedBinding

class PausedCallView : LinearLayout {
    private lateinit var binding: CallPausedBinding

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(
        context,
        attrs
    ) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context) {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.call_paused, this, true
        )
    }

    fun setViewModel(viewModel: CallViewModel) {
        binding.viewModel = viewModel

        binding.callTimer.base =
            SystemClock.elapsedRealtime() - (1000 * viewModel.call.duration) // Linphone timestamps are in seconds
        binding.callTimer.start()
    }
}
