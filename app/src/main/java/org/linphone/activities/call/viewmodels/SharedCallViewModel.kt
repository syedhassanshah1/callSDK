
package org.linphone.activities.call.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.linphone.utils.Event

class SharedCallViewModel : ViewModel() {
    val toggleDrawerEvent = MutableLiveData<Event<Boolean>>()

    val resetHiddenInterfaceTimerInVideoCallEvent = MutableLiveData<Event<Boolean>>()
}
