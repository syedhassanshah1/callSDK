
package org.linphone.activities.call.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.linphone.BaseApplication.Companion.coreContext
import org.linphone.core.AudioDevice
import org.linphone.core.Call
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub
import org.linphone.core.tools.Log

class ControlsFadingViewModel : ViewModel() {
    val areControlsHidden = MutableLiveData<Boolean>()

    val isVideoPreviewHidden = MutableLiveData<Boolean>()
    val isVideoPreviewResizedForPip = MutableLiveData<Boolean>()

    private val videoEnabled = MutableLiveData<Boolean>()
    private val nonEarpieceOutputAudioDevice = MutableLiveData<Boolean>()
    val proximitySensorEnabled: MediatorLiveData<Boolean> = MediatorLiveData()

    private var timer: Timer? = null

    private val listener = object : CoreListenerStub() {
        override fun onCallStateChanged(
            core: Core,
            call: Call,
            state: Call.State,
            message: String
        ) {
            if (state == Call.State.StreamsRunning || state == Call.State.Updating || state == Call.State.UpdatedByRemote) {
                val isVideoCall = coreContext.isVideoCallOrConferenceActive()
                Log.i("[Controls Fading] Call is in state $state, video is ${if (isVideoCall) "enabled" else "disabled"}")
                if (isVideoCall) {
                    videoEnabled.value = true
                    startTimer()
                } else {
                    videoEnabled.value = false
                    stopTimer()
                }
            }
        }

        override fun onAudioDeviceChanged(core: Core, audioDevice: AudioDevice) {
            if (audioDevice.hasCapability(AudioDevice.Capabilities.CapabilityPlay)) {
                Log.i("[Controls Fading] Output audio device changed to: ${audioDevice.id}")
                nonEarpieceOutputAudioDevice.value = audioDevice.type != AudioDevice.Type.Earpiece
            }
        }
    }

    init {
        coreContext.core.addListener(listener)

        areControlsHidden.value = false
        isVideoPreviewHidden.value = false
        isVideoPreviewResizedForPip.value = false
        nonEarpieceOutputAudioDevice.value = coreContext.core.outputAudioDevice?.type != AudioDevice.Type.Earpiece

        val isVideoCall = coreContext.isVideoCallOrConferenceActive()
        videoEnabled.value = isVideoCall
        if (isVideoCall) {
            startTimer()
        }

        proximitySensorEnabled.value = shouldEnableProximitySensor()
        proximitySensorEnabled.addSource(videoEnabled) {
            proximitySensorEnabled.value = shouldEnableProximitySensor()
        }
        proximitySensorEnabled.addSource(nonEarpieceOutputAudioDevice) {
            proximitySensorEnabled.value = shouldEnableProximitySensor()
        }
    }

    override fun onCleared() {
        coreContext.core.removeListener(listener)
        stopTimer()

        super.onCleared()
    }

    fun showMomentarily() {
        stopTimer()
        startTimer()
    }

    private fun shouldEnableProximitySensor(): Boolean {
        return !(videoEnabled.value ?: false) && !(nonEarpieceOutputAudioDevice.value ?: false)
    }

    private fun stopTimer() {
        timer?.cancel()

        areControlsHidden.value = false
    }

    private fun startTimer() {
        timer?.cancel()

        timer = Timer("Hide UI controls scheduler")
        timer?.schedule(object : TimerTask() {
            override fun run() {
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        val videoEnabled = coreContext.isVideoCallOrConferenceActive()
                        areControlsHidden.postValue(videoEnabled)
                    }
                }
            }
        }, 3000)
    }
}
