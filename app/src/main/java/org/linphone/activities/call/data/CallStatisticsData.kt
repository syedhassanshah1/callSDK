
package org.linphone.activities.call.data

import androidx.lifecycle.MutableLiveData
import org.linphone.BaseApplication.Companion.coreContext
import org.linphone.contact.GenericContactData
import org.linphone.core.*

class CallStatisticsData(val call: Call) : GenericContactData(call.remoteAddress) {
    val audioStats = MutableLiveData<ArrayList<StatItemData>>()

    val videoStats = MutableLiveData<ArrayList<StatItemData>>()

    val isVideoEnabled = MutableLiveData<Boolean>()

    val isExpanded = MutableLiveData<Boolean>()

    private val listener = object : CoreListenerStub() {
        override fun onCallStatsUpdated(core: Core, call: Call, stats: CallStats) {
            if (call == this@CallStatisticsData.call) {
                isVideoEnabled.value = call.currentParams.videoEnabled()
                updateCallStats(stats)
            }
        }
    }

    init {
        coreContext.core.addListener(listener)

        audioStats.value = arrayListOf()
        videoStats.value = arrayListOf()

        initCallStats()

        val videoEnabled = call.currentParams.videoEnabled()
        isVideoEnabled.value = videoEnabled

        isExpanded.value = coreContext.core.currentCall == call
    }

    override fun destroy() {
        coreContext.core.removeListener(listener)
        super.destroy()
    }

    fun toggleExpanded() {
        isExpanded.value = isExpanded.value != true
    }

    private fun initCallStats() {
        val audioList = arrayListOf<StatItemData>()
        audioList.add(StatItemData(StatType.CAPTURE))
        audioList.add(StatItemData(StatType.PLAYBACK))
        audioList.add(StatItemData(StatType.PAYLOAD))
        audioList.add(StatItemData(StatType.ENCODER))
        audioList.add(StatItemData(StatType.DECODER))
        audioList.add(StatItemData(StatType.DOWNLOAD_BW))
        audioList.add(StatItemData(StatType.UPLOAD_BW))
        audioList.add(StatItemData(StatType.ICE))
        audioList.add(StatItemData(StatType.IP_FAM))
        audioList.add(StatItemData(StatType.SENDER_LOSS))
        audioList.add(StatItemData(StatType.RECEIVER_LOSS))
        audioList.add(StatItemData(StatType.JITTER))
        audioStats.value = audioList

        val videoList = arrayListOf<StatItemData>()
        videoList.add(StatItemData(StatType.CAPTURE))
        videoList.add(StatItemData(StatType.PLAYBACK))
        videoList.add(StatItemData(StatType.PAYLOAD))
        videoList.add(StatItemData(StatType.ENCODER))
        videoList.add(StatItemData(StatType.DECODER))
        videoList.add(StatItemData(StatType.DOWNLOAD_BW))
        videoList.add(StatItemData(StatType.UPLOAD_BW))
        videoList.add(StatItemData(StatType.ESTIMATED_AVAILABLE_DOWNLOAD_BW))
        videoList.add(StatItemData(StatType.ICE))
        videoList.add(StatItemData(StatType.IP_FAM))
        videoList.add(StatItemData(StatType.SENDER_LOSS))
        videoList.add(StatItemData(StatType.RECEIVER_LOSS))
        videoList.add(StatItemData(StatType.SENT_RESOLUTION))
        videoList.add(StatItemData(StatType.RECEIVED_RESOLUTION))
        videoList.add(StatItemData(StatType.SENT_FPS))
        videoList.add(StatItemData(StatType.RECEIVED_FPS))
        videoStats.value = videoList
    }

    private fun updateCallStats(stats: CallStats) {
        if (stats.type == StreamType.Audio) {
            for (stat in audioStats.value.orEmpty()) {
                stat.update(call, stats)
            }
        } else if (stats.type == StreamType.Video) {
            for (stat in videoStats.value.orEmpty()) {
                stat.update(call, stats)
            }
        }
    }
}
