
package org.linphone.activities.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList
import org.linphone.BaseApplication
import org.linphone.contact.Contact
import org.linphone.core.*
import org.linphone.core.tools.Log
import org.linphone.utils.Event

class SharedMainViewModel : ViewModel() {
    val toggleDrawerEvent = MutableLiveData<Event<Boolean>>()

    /* Call history */

    /* Chat */

    val selectedChatRoom = MutableLiveData<ChatRoom>()
    var destructionPendingChatRoom: ChatRoom? = null

    val selectedGroupChatRoom = MutableLiveData<ChatRoom>()

    val filesToShare = MutableLiveData<ArrayList<String>>()

    val textToShare = MutableLiveData<String>()

    val messageToForwardEvent: MutableLiveData<Event<ChatMessage>> by lazy {
        MutableLiveData<Event<ChatMessage>>()
    }

    val contentToOpen = MutableLiveData<Content>()

    var createEncryptedChatRoom: Boolean = false

    val chatRoomParticipants = MutableLiveData<ArrayList<Address>>()

    /* Contacts */

    val selectedContact = MutableLiveData<Contact>()

    /* Accounts */

    val accountRemoved = MutableLiveData<Boolean>()

    /* Call */

    var pendingCallTransfer: Boolean = false

    /* Dialer */

    var dialerUri: String = ""

    val remoteProvisioningUrl = MutableLiveData<String>()

    private var accountCreator: AccountCreator
    private var useGenericSipAccount: Boolean = false

    init {
        Log.i("[Assistant] Loading linphone default values")
        BaseApplication.coreContext.core.loadConfigFromXml(BaseApplication.corePreferences.linphoneDefaultValuesPath)
        accountCreator = BaseApplication.coreContext.core.createAccountCreator(BaseApplication.corePreferences.xmlRpcServerUrl)
        accountCreator.language = Locale.getDefault().language
    }

    fun getAccountCreator(genericAccountCreator: Boolean = false): AccountCreator {
        if (genericAccountCreator != useGenericSipAccount) {
            accountCreator.reset()
            accountCreator.language = Locale.getDefault().language

            if (genericAccountCreator) {
                Log.i("[Assistant] Loading default values")
                BaseApplication.coreContext.core.loadConfigFromXml(BaseApplication.corePreferences.defaultValuesPath)
            } else {
                Log.i("[Assistant] Loading linphone default values")
//                BaseApplication.coreContext.core.loadConfigFromXml(BaseApplication.corePreferences.linphoneDefaultValuesPath)
            }
            useGenericSipAccount = genericAccountCreator
        }
        return accountCreator
    }
}
