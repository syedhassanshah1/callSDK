
package org.linphone.activities.call.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.linphone.BaseApplication.Companion.coreContext
import org.linphone.activities.call.data.CallStatisticsData
import org.linphone.core.Call
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub

class StatisticsListViewModel : ViewModel() {
    val callStatsList = MutableLiveData<ArrayList<CallStatisticsData>>()

    private val listener = object : CoreListenerStub() {
        override fun onCallStateChanged(
            core: Core,
            call: Call,
            state: Call.State,
            message: String
        ) {
            if (state == Call.State.End || state == Call.State.Error || state == Call.State.Connected) {
                computeCallsList()
            }
        }
    }

    init {
        coreContext.core.addListener(listener)

        computeCallsList()
    }

    override fun onCleared() {
        callStatsList.value.orEmpty().forEach(CallStatisticsData::destroy)
        coreContext.core.removeListener(listener)

        super.onCleared()
    }

    private fun computeCallsList() {
        val list = arrayListOf<CallStatisticsData>()
        for (call in coreContext.core.calls) {
            if (call.state != Call.State.End && call.state != Call.State.Released && call.state != Call.State.Error) {
                list.add(CallStatisticsData(call))
            }
        }
        callStatsList.value = list
    }
}
