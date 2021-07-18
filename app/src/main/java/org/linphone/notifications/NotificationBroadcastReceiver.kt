
package org.linphone.notifications

import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.linphone.BaseApplication.Companion.coreContext
import org.linphone.core.Call
import org.linphone.core.Core
import org.linphone.core.tools.Log

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(NotificationsManager.INTENT_NOTIF_ID, 0)

        if (intent.action == NotificationsManager.INTENT_REPLY_NOTIF_ACTION || intent.action == NotificationsManager.INTENT_MARK_AS_READ_ACTION) {
            handleChatIntent(intent, notificationId)
        } else if (intent.action == NotificationsManager.INTENT_ANSWER_CALL_NOTIF_ACTION || intent.action == NotificationsManager.INTENT_HANGUP_CALL_NOTIF_ACTION) {
            handleCallIntent(intent)
        }
    }

    private fun handleChatIntent(intent: Intent, notificationId: Int) {
        val remoteSipAddress = intent.getStringExtra(NotificationsManager.INTENT_REMOTE_ADDRESS)
        if (remoteSipAddress == null) {
            Log.e("[Notification Broadcast Receiver] Remote SIP address is null for notification id $notificationId")
            return
        }
        val core: Core = coreContext.core

        val remoteAddress = core.interpretUrl(remoteSipAddress)
        if (remoteAddress == null) {
            Log.e("[Notification Broadcast Receiver] Couldn't interpret remote address $remoteSipAddress")
            return
        }

        val localIdentity = intent.getStringExtra(NotificationsManager.INTENT_LOCAL_IDENTITY)
        if (localIdentity == null) {
            Log.e("[Notification Broadcast Receiver] Local identity is null for notification id $notificationId")
            return
        }
        val localAddress = core.interpretUrl(localIdentity)
        if (localAddress == null) {
            Log.e("[Notification Broadcast Receiver] Couldn't interpret local address $localIdentity")
            return
        }

        val room = core.searchChatRoom(null, localAddress, remoteAddress, arrayOfNulls(0))
        if (room == null) {
            Log.e("[Notification Broadcast Receiver] Couldn't find chat room for remote address $remoteSipAddress and local address $localIdentity")
            return
        }

        room.markAsRead()
        if (intent.action == NotificationsManager.INTENT_REPLY_NOTIF_ACTION) {
            val reply = getMessageText(intent)?.toString()
            if (reply == null) {
                Log.e("[Notification Broadcast Receiver] Couldn't get reply text")
                return
            }

            val msg = room.createMessageFromUtf8(reply)
            msg.userData = notificationId
            msg.addListener(coreContext.notificationsManager.chatListener)
            msg.send()
            Log.i("[Notification Broadcast Receiver] Reply sent for notif id $notificationId")
        } else {
            coreContext.notificationsManager.dismissChatNotification(room)
        }
    }

    private fun handleCallIntent(intent: Intent) {
        val remoteSipAddress = intent.getStringExtra(NotificationsManager.INTENT_REMOTE_ADDRESS)
        if (remoteSipAddress == null) {
            Log.e("[Notification Broadcast Receiver] Remote SIP address is null for notification")
            return
        }

        val core: Core = coreContext.core

        val remoteAddress = core.interpretUrl(remoteSipAddress)
        val call = if (remoteAddress != null) core.getCallByRemoteAddress2(remoteAddress) else null
        if (call == null) {
            Log.e("[Notification Broadcast Receiver] Couldn't find call from remote address $remoteSipAddress")
            return
        }

        if (intent.action == NotificationsManager.INTENT_ANSWER_CALL_NOTIF_ACTION) {
            coreContext.answerCall(call)
        } else {
            if (call.state == Call.State.IncomingReceived || call.state == Call.State.IncomingEarlyMedia) coreContext.declineCall(call) else coreContext.terminateCall(call)
        }
    }

    private fun getMessageText(intent: Intent): CharSequence? {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        return remoteInput?.getCharSequence(NotificationsManager.KEY_TEXT_REPLY)
    }
}
