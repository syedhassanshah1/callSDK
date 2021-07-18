
package org.linphone.activities.main.viewmodels

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.linphone.BaseApplication.Companion.coreContext
import org.linphone.BaseApplication.Companion.corePreferences
import org.linphone.R
import org.linphone.core.*
import org.linphone.utils.AppUtils

class TabsViewModel : ViewModel() {
    val unreadMessagesCount = MutableLiveData<Int>()
    val missedCallsCount = MutableLiveData<Int>()

    val leftAnchor = MutableLiveData<Float>()
    val middleAnchor = MutableLiveData<Float>()
    val rightAnchor = MutableLiveData<Float>()

    val historyMissedCountTranslateY = MutableLiveData<Float>()
    val chatUnreadCountTranslateY = MutableLiveData<Float>()

    private val bounceAnimator: ValueAnimator by lazy {
        ValueAnimator.ofFloat(AppUtils.getDimension(R.dimen.tabs_fragment_unread_count_bounce_offset), 0f).apply {
            addUpdateListener {
                val value = it.animatedValue as Float
                historyMissedCountTranslateY.value = -value
                chatUnreadCountTranslateY.value = -value
            }
            interpolator = LinearInterpolator()
            duration = 250
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
        }
    }

    private val listener: CoreListenerStub = object : CoreListenerStub() {
        override fun onCallStateChanged(
            core: Core,
            call: Call,
            state: Call.State,
            message: String
        ) {
            if (state == Call.State.End || state == Call.State.Error) {
                updateMissedCallCount()
            }
        }

        override fun onChatRoomRead(core: Core, chatRoom: ChatRoom) {
            updateUnreadChatCount()
        }

        override fun onMessageReceived(core: Core, chatRoom: ChatRoom, message: ChatMessage) {
            updateUnreadChatCount()
        }

        override fun onChatRoomStateChanged(core: Core, chatRoom: ChatRoom, state: ChatRoom.State) {
            if (state == ChatRoom.State.Deleted) {
                updateUnreadChatCount()
            }
        }
    }

    init {
        coreContext.core.addListener(listener)

        if (corePreferences.disableChat) {
            leftAnchor.value = 1 / 3F
            middleAnchor.value = 2 / 3F
            rightAnchor.value = 1F
        } else {
            leftAnchor.value = 0.25F
            middleAnchor.value = 0.5F
            rightAnchor.value = 0.75F
        }

        updateUnreadChatCount()
        updateMissedCallCount()

        if (corePreferences.enableAnimations) bounceAnimator.start()
    }

    override fun onCleared() {
        coreContext.core.removeListener(listener)
        super.onCleared()
    }

    fun updateMissedCallCount() {
        missedCallsCount.value = coreContext.core.missedCallsCount
    }

    fun updateUnreadChatCount() {
        unreadMessagesCount.value = if (corePreferences.disableChat) 0 else coreContext.core.unreadChatMessageCountFromActiveLocals
    }
}
