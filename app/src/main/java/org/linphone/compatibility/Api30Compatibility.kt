
package org.linphone.compatibility

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.ShortcutManager
import org.linphone.core.ChatRoom
import org.linphone.utils.LinphoneUtils

@TargetApi(30)
class Api30Compatibility {
    companion object {
        fun removeChatRoomShortcut(context: Context, chatRoom: ChatRoom) {
            val peerAddress = chatRoom.peerAddress.asStringUriOnly()
            val localAddress = chatRoom.localAddress.asStringUriOnly()

            val shortcutManager = context.getSystemService(ShortcutManager::class.java)
            val id = LinphoneUtils.getChatRoomId(localAddress, peerAddress)
            val shortcutsToRemoveList = arrayListOf(id)
            shortcutManager.removeLongLivedShortcuts(shortcutsToRemoveList)
        }
    }
}
