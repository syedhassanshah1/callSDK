
package org.linphone.activities.call.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.linphone.BaseApplication.Companion.coreContext
import org.linphone.compatibility.Compatibility
import org.linphone.contact.GenericContactViewModel
import org.linphone.core.Call
import org.linphone.core.CallListenerStub
import org.linphone.core.Factory
import org.linphone.core.tools.Log
import org.linphone.utils.Event
import org.linphone.utils.FileUtils
import org.linphone.utils.LinphoneUtils

class CallViewModelFactory(private val call: Call) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CallViewModel(call) as T
    }
}

open class CallViewModel(val call: Call) : GenericContactViewModel(call.remoteAddress) {
    val address: String by lazy {
        val copy = call.remoteAddress.clone()
        copy.clean() // To remove gruu if any
        LinphoneUtils.getDisplayableAddress(copy)
    }

    val isPaused = MutableLiveData<Boolean>()

    val isOutgoingEarlyMedia = MutableLiveData<Boolean>()

    val callEndedEvent: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    val callConnectedEvent: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    private var timer: Timer? = null

    private val listener = object : CallListenerStub() {
        override fun onStateChanged(call: Call, state: Call.State, message: String) {
            if (call != this@CallViewModel.call) return

            isPaused.value = state == Call.State.Paused
            isOutgoingEarlyMedia.value = state == Call.State.OutgoingEarlyMedia

            if (state == Call.State.End || state == Call.State.Released || state == Call.State.Error) {
                timer?.cancel()
                callEndedEvent.value = Event(true)

                if (state == Call.State.Error) {
                    Log.e("[Call View Model] Error state reason is ${call.reason}")
                }
            } else if (call.state == Call.State.Connected) {
                callConnectedEvent.value = Event(true)
            } else if (call.state == Call.State.StreamsRunning) {
                // Stop call update timer once user has accepted or declined call update
                timer?.cancel()
            } else if (call.state == Call.State.UpdatedByRemote) {
                // User has 30 secs to accept or decline call update
                // Dialog to accept or decline is handled by CallsViewModel & ControlsFragment
                startTimer(call)
            }
        }

        override fun onSnapshotTaken(call: Call, filePath: String) {
            Log.i("[Call View Model] Snapshot taken, saved at $filePath")
            val content = Factory.instance().createContent()
            content.filePath = filePath
            content.type = "image"
            content.subtype = "jpeg"
            content.name = filePath.substring(filePath.indexOf("/") + 1)

            viewModelScope.launch {
                if (Compatibility.addImageToMediaStore(coreContext.context, content)) {
                    Log.i("[Call View Model] Adding snapshot ${content.name} to Media Store terminated")
                } else {
                    Log.e("[Call View Model] Something went wrong while copying file to Media Store...")
                }
            }
        }
    }

    init {
        call.addListener(listener)

        isPaused.value = call.state == Call.State.Paused
    }

    override fun onCleared() {
        destroy()
        super.onCleared()
    }

    fun destroy() {
        call.removeListener(listener)
    }

    fun terminateCall() {
        coreContext.terminateCall(call)
    }

    fun pause() {
        call.pause()
    }

    fun resume() {
        call.resume()
    }

    fun takeScreenshot() {
        if (call.currentParams.videoEnabled()) {
            val fileName = System.currentTimeMillis().toString() + ".jpeg"
            call.takeVideoSnapshot(FileUtils.getFileStoragePath(fileName).absolutePath)
        }
    }

    private fun startTimer(call: Call) {
        timer?.cancel()

        timer = Timer("Call update timeout")
        timer?.schedule(object : TimerTask() {
            override fun run() {
                // Decline call update
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        coreContext.answerCallVideoUpdateRequest(call, false)
                    }
                }
            }
        }, 30000)
    }
}
