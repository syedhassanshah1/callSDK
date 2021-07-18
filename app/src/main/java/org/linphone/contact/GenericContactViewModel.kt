
package org.linphone.contact

import androidx.lifecycle.MutableLiveData
import org.linphone.BaseApplication.Companion.coreContext
import org.linphone.activities.main.viewmodels.ErrorReportingViewModel
import org.linphone.core.Address
import org.linphone.utils.LinphoneUtils

abstract class GenericContactViewModel(private val sipAddress: Address) : ErrorReportingViewModel(), ContactDataInterface {
    override val displayName: String = LinphoneUtils.getDisplayName(sipAddress)

    override val contact = MutableLiveData<Contact>()

    private val contactsUpdatedListener = object : ContactsUpdatedListenerStub() {
        override fun onContactUpdated(contact: Contact) {
            contactLookup()
        }
    }

    init {
        coreContext.contactsManager.addListener(contactsUpdatedListener)
        contactLookup()
    }

    override fun onCleared() {
        coreContext.contactsManager.removeListener(contactsUpdatedListener)

        super.onCleared()
    }

    private fun contactLookup() {
        contact.value = coreContext.contactsManager.findContactByAddress(sipAddress)
    }
}
