

package org.linphone.activities.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.linphone.BaseApplication.Companion.coreContext
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub
import org.linphone.utils.Event

open class LogsUploadViewModel : ViewModel() {
    val uploadInProgress = MutableLiveData<Boolean>()

    val resetCompleteEvent: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    val uploadFinishedEvent: MutableLiveData<Event<String>> by lazy {
        MutableLiveData<Event<String>>()
    }

    val uploadErrorEvent: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    private val listener = object : CoreListenerStub() {
        override fun onLogCollectionUploadStateChanged(
            core: Core,
            state: Core.LogCollectionUploadState,
            info: String
        ) {
            if (state == Core.LogCollectionUploadState.Delivered) {
                uploadInProgress.value = false
                uploadFinishedEvent.value = Event(info)
            } else if (state == Core.LogCollectionUploadState.NotDelivered) {
                uploadInProgress.value = false
                uploadErrorEvent.value = Event(true)
            }
        }
    }

    init {
        coreContext.core.addListener(listener)
        uploadInProgress.value = false
    }

    override fun onCleared() {
        coreContext.core.removeListener(listener)

        super.onCleared()
    }

    fun uploadLogs() {
        uploadInProgress.value = true
        coreContext.core.uploadLogCollection()
    }

    fun resetLogs() {
        coreContext.core.resetLogCollection()
        resetCompleteEvent.value = Event(true)
    }
}
