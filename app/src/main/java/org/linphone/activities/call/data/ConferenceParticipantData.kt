
package org.linphone.activities.call.data

import androidx.lifecycle.MutableLiveData
import org.linphone.contact.GenericContactData
import org.linphone.core.Conference
import org.linphone.core.Participant
import org.linphone.core.tools.Log

class ConferenceParticipantData(
    private val conference: Conference,
    val participant: Participant
) :
    GenericContactData(participant.address) {
    private val isAdmin = MutableLiveData<Boolean>()
    val isMeAdmin = MutableLiveData<Boolean>()

    init {
        isAdmin.value = participant.isAdmin
        isMeAdmin.value = conference.me.isAdmin
        Log.i("[Conference Participant VM] Participant ${participant.address.asStringUriOnly()} is ${if (participant.isAdmin) "admin" else "not admin"}")
        Log.i("[Conference Participant VM] Me is ${if (conference.me.isAdmin) "admin" else "not admin"} and is ${if (conference.me.isFocus) "focus" else "not focus"}")
    }

    fun removeFromConference() {
        Log.i("[Conference Participant VM] Removing participant ${participant.address.asStringUriOnly()} from conference $conference")
        conference.removeParticipant(participant)
    }
}
