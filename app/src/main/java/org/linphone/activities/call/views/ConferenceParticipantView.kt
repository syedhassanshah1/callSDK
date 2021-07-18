
package org.linphone.activities.call.views

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import org.linphone.R
import org.linphone.activities.call.data.ConferenceParticipantData
import org.linphone.core.tools.Log
import org.linphone.databinding.CallConferenceParticipantBinding

class ConferenceParticipantView : LinearLayout {
    private lateinit var binding: CallConferenceParticipantBinding

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
            LayoutInflater.from(context), R.layout.call_conference_participant, this, true
        )
    }

    fun setData(data: ConferenceParticipantData) {
        binding.data = data

        val currentTimeSecs = System.currentTimeMillis()
        val participantTime = data.participant.creationTime * 1000 // Linphone timestamps are in seconds
        val diff = currentTimeSecs - participantTime
        Log.i("[Conference Participant] Participant joined conference at $participantTime == ${diff / 1000} seconds ago.")
        binding.callTimer.base = SystemClock.elapsedRealtime() - diff
        binding.callTimer.start()
    }
}
