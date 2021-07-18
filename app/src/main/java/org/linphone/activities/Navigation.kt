/*
package org.linphone.activities

import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import org.callSDK.activities.main.dialer.fragments.DialerFragment
import org.linphone.BaseApplication.Companion.corePreferences
import org.linphone.R
import org.linphone.activities.main.MainActivity
import org.linphone.activities.main.chat.fragments.ChatRoomCreationFragment
import org.linphone.activities.main.chat.fragments.DetailChatRoomFragment
import org.linphone.activities.main.chat.fragments.GroupInfoFragment
import org.linphone.activities.main.chat.fragments.MasterChatRoomsFragment
import org.linphone.activities.main.contact.fragments.ContactEditorFragment
import org.linphone.activities.main.contact.fragments.DetailContactFragment
import org.linphone.activities.main.contact.fragments.MasterContactsFragment
import org.linphone.activities.main.dialer.fragments.DialerFragment
import org.linphone.activities.main.fragments.TabsFragment
import org.linphone.activities.main.history.fragments.DetailCallLogFragment
import org.linphone.activities.main.history.fragments.MasterCallLogsFragment
import org.linphone.activities.main.settings.fragments.AccountSettingsFragment
import org.linphone.activities.main.settings.fragments.SettingsFragment
import org.linphone.activities.main.sidemenu.fragments.SideMenuFragment
import org.linphone.contact.NativeContact
import org.linphone.core.Address

internal fun Fragment.findMasterNavController(): NavController {
    return if (!resources.getBoolean(R.bool.isTablet)) {
        findNavController()
    } else {
        parentFragment?.parentFragment?.findNavController() ?: findNavController()
    }
}

fun getRightToLeftAnimationNavOptions(
        popUpTo: Int = -1,
        popUpInclusive: Boolean = false,
        singleTop: Boolean = true
): NavOptions {
    val builder = NavOptions.Builder()
    builder.setPopUpTo(popUpTo, popUpInclusive).setLaunchSingleTop(singleTop)
    if (!corePreferences.enableAnimations) return builder.build()
    return builder
            .setEnterAnim(R.anim.enter_right)
            .setExitAnim(R.anim.exit_left)
            .setPopEnterAnim(R.anim.enter_left)
            .setPopExitAnim(R.anim.exit_right)
            .build()
}

fun getLeftToRightAnimationNavOptions(
        popUpTo: Int = -1,
        popUpInclusive: Boolean = false,
        singleTop: Boolean = true
): NavOptions {
    val builder = NavOptions.Builder()
    builder.setPopUpTo(popUpTo, popUpInclusive).setLaunchSingleTop(singleTop)
    if (!corePreferences.enableAnimations) return builder.build()
    return builder
            .setEnterAnim(R.anim.enter_left)
            .setExitAnim(R.anim.exit_right)
            .setPopEnterAnim(R.anim.enter_right)
            .setPopExitAnim(R.anim.exit_left)
            .build()
}

fun getRightBottomToLeftTopAnimationNavOptions(
        popUpTo: Int = -1,
        popUpInclusive: Boolean = false,
        singleTop: Boolean = true
): NavOptions {
    val builder = NavOptions.Builder()
    builder.setPopUpTo(popUpTo, popUpInclusive).setLaunchSingleTop(singleTop)
    if (!corePreferences.enableAnimations) return builder.build()
    return builder
            .setEnterAnim(R.anim.enter_right_or_bottom)
            .setExitAnim(R.anim.exit_left_or_top)
            .setPopEnterAnim(R.anim.enter_left_or_top)
            .setPopExitAnim(R.anim.exit_right_or_bottom)
            .build()
}

fun getLeftTopToRightBottomAnimationNavOptions(
        popUpTo: Int = -1,
        popUpInclusive: Boolean = false,
        singleTop: Boolean = true
): NavOptions {
    val builder = NavOptions.Builder()
    builder.setPopUpTo(popUpTo, popUpInclusive).setLaunchSingleTop(singleTop)
    if (!corePreferences.enableAnimations) return builder.build()
    return builder
            .setEnterAnim(R.anim.enter_left_or_top)
            .setExitAnim(R.anim.exit_right_or_bottom)
            .setPopEnterAnim(R.anim.enter_right_or_bottom)
            .setPopExitAnim(R.anim.exit_left_or_top)
            .build()
}

fun getRightBottomToLeftTopNoPopAnimationNavOptions(
        popUpTo: Int = -1,
        popUpInclusive: Boolean = false,
        singleTop: Boolean = true
): NavOptions {
    val builder = NavOptions.Builder()
    builder.setPopUpTo(popUpTo, popUpInclusive).setLaunchSingleTop(singleTop)
    if (!corePreferences.enableAnimations) return builder.build()
    return builder
            .setEnterAnim(R.anim.enter_right_or_bottom)
            .setExitAnim(R.anim.exit_left_or_top)
            .build()
}

fun getLeftTopToRightBottomNoPopAnimationNavOptions(
        popUpTo: Int = -1,
        popUpInclusive: Boolean = false,
        singleTop: Boolean = true
): NavOptions {
    val builder = NavOptions.Builder()
    builder.setPopUpTo(popUpTo, popUpInclusive).setLaunchSingleTop(singleTop)
    if (!corePreferences.enableAnimations) return builder.build()
    return builder
            .setEnterAnim(R.anim.enter_left_or_top)
            .setExitAnim(R.anim.exit_right_or_bottom)
            .build()
}

*/
/* Main activity related *//*


internal fun MainActivity.navigateToDialer(args: Bundle?) {

}

*/
/* Tabs fragment related *//*



*/
/* Dialer related *//*


internal fun DialerFragment.navigateToContacts(uriToAdd: String?) {
    val deepLink = "linphone-android://contact/new/$uriToAdd"
    findNavController().navigate(
            Uri.parse(deepLink),
            getLeftTopToRightBottomNoPopAnimationNavOptions(R.id.masterContactsFragment, true)
    )
}

internal fun DialerFragment.navigateToConfigFileViewer() {
    val bundle = bundleOf("Secure" to true)
    findMasterNavController().navigate(
            R.id.action_global_configViewerFragment,
            bundle,
            getRightToLeftAnimationNavOptions()
    )
}

*/
/* Chat related *//*


internal fun MasterChatRoomsFragment.navigateToChatRoom(args: Bundle) {
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.masterChatRoomsFragment) {
            findNavController().navigate(
                    R.id.action_masterChatRoomsFragment_to_detailChatRoomFragment,
                    args,
                    getRightToLeftAnimationNavOptions()
            )
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.chat_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_detailChatRoomFragment,
                args,
                getRightToLeftAnimationNavOptions(R.id.emptyChatFragment, true)
        )
    }
}

internal fun MasterChatRoomsFragment.navigateToChatRoomCreation(
        createGroupChatRoom: Boolean = false
) {
    val bundle = bundleOf("createGroup" to createGroupChatRoom)
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.masterChatRoomsFragment) {
            findNavController().navigate(
                    R.id.action_masterChatRoomsFragment_to_chatRoomCreationFragment,
                    bundle,
                    getRightToLeftAnimationNavOptions()
            )
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.chat_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_chatRoomCreationFragment,
                bundle,
                getRightToLeftAnimationNavOptions(R.id.emptyChatFragment, true)
        )
    }
}

internal fun DetailChatRoomFragment.navigateToContacts(sipUriToAdd: String) {
    val deepLink = "linphone-android://contact/new/$sipUriToAdd"
    findMasterNavController().navigate(Uri.parse(deepLink), getLeftToRightAnimationNavOptions())
}

internal fun DetailChatRoomFragment.navigateToChatRooms() {
    findMasterNavController().navigate(
            R.id.action_global_masterChatRoomsFragment,
            null,
            getLeftToRightAnimationNavOptions(R.id.masterChatRoomsFragment)
    )
}

internal fun DetailChatRoomFragment.navigateToImdn(args: Bundle?) {
    if (findNavController().currentDestination?.id == R.id.detailChatRoomFragment) {
        findNavController().navigate(
                R.id.action_detailChatRoomFragment_to_imdnFragment,
                args,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun DetailChatRoomFragment.navigateToDevices() {
    if (findNavController().currentDestination?.id == R.id.detailChatRoomFragment) {
        findNavController().navigate(
                R.id.action_detailChatRoomFragment_to_devicesFragment,
                null,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun DetailChatRoomFragment.navigateToGroupInfo() {
    if (findNavController().currentDestination?.id == R.id.detailChatRoomFragment) {
        findNavController().navigate(
                R.id.action_detailChatRoomFragment_to_groupInfoFragment,
                null,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun DetailChatRoomFragment.navigateToEphemeralInfo() {
    if (findNavController().currentDestination?.id == R.id.detailChatRoomFragment) {
        findNavController().navigate(
                R.id.action_detailChatRoomFragment_to_ephemeralFragment,
                null,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun DetailChatRoomFragment.navigateToTextFileViewer(secure: Boolean) {
    val bundle = bundleOf("Secure" to secure)
    findMasterNavController().navigate(
            R.id.action_global_textViewerFragment,
            bundle,
            getRightToLeftAnimationNavOptions()
    )
}

internal fun DetailChatRoomFragment.navigateToPdfFileViewer(secure: Boolean) {
    val bundle = bundleOf("Secure" to secure)
    findMasterNavController().navigate(
            R.id.action_global_pdfViewerFragment,
            bundle,
            getRightToLeftAnimationNavOptions()
    )
}

internal fun DetailChatRoomFragment.navigateToImageFileViewer(secure: Boolean) {
    val bundle = bundleOf("Secure" to secure)
    findMasterNavController().navigate(
            R.id.action_global_imageViewerFragment,
            bundle,
            getRightToLeftAnimationNavOptions()
    )
}

internal fun DetailChatRoomFragment.navigateToVideoFileViewer(secure: Boolean) {
    val bundle = bundleOf("Secure" to secure)
    findMasterNavController().navigate(
            R.id.action_global_videoViewerFragment,
            bundle,
            getRightToLeftAnimationNavOptions()
    )
}

internal fun DetailChatRoomFragment.navigateToAudioFileViewer(secure: Boolean) {
    val bundle = bundleOf("Secure" to secure)
    findMasterNavController().navigate(
            R.id.action_global_audioViewerFragment,
            bundle,
            getRightToLeftAnimationNavOptions()
    )
}

internal fun ChatRoomCreationFragment.navigateToGroupInfo() {
    if (findNavController().currentDestination?.id == R.id.chatRoomCreationFragment) {
        findNavController().navigate(
                R.id.action_chatRoomCreationFragment_to_groupInfoFragment,
                null,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun ChatRoomCreationFragment.navigateToChatRoom() {
    if (findNavController().currentDestination?.id == R.id.chatRoomCreationFragment) {
        if (!resources.getBoolean(R.bool.isTablet)) {
            findNavController().navigate(
                    R.id.action_chatRoomCreationFragment_to_detailChatRoomFragment,
                    null,
                    getRightToLeftAnimationNavOptions()
            )
        } else {
            findNavController().navigate(
                    R.id.action_chatRoomCreationFragment_to_detailChatRoomFragment,
                    null,
                    getRightToLeftAnimationNavOptions(R.id.emptyFragment, true)
            )
        }
    }
}

internal fun GroupInfoFragment.navigateToChatRoomCreation(args: Bundle?) {
    if (findNavController().currentDestination?.id == R.id.groupInfoFragment) {
        findNavController().navigate(R.id.action_groupInfoFragment_to_chatRoomCreationFragment,
                args,
                getLeftToRightAnimationNavOptions(R.id.chatRoomCreationFragment, true)
        )
    }
}

internal fun GroupInfoFragment.navigateToChatRoom() {
    if (findNavController().currentDestination?.id == R.id.groupInfoFragment) {
        if (!resources.getBoolean(R.bool.isTablet)) {
            findNavController().navigate(
                    R.id.action_groupInfoFragment_to_detailChatRoomFragment,
                    null,
                    getRightToLeftAnimationNavOptions()
            )
        } else {
            findNavController().navigate(
                    R.id.action_groupInfoFragment_to_detailChatRoomFragment,
                    null,
                    getRightToLeftAnimationNavOptions(R.id.emptyFragment, true)
            )
        }
    }
}

*/
/* Contacts related *//*


internal fun MasterContactsFragment.navigateToContact() {
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.masterContactsFragment) {
            findNavController().navigate(R.id.action_masterContactsFragment_to_detailContactFragment,
                    null,
                    getRightToLeftAnimationNavOptions())
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.contacts_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_detailContactFragment,
                null,
                getRightToLeftAnimationNavOptions(R.id.emptyContactFragment, true)
        )
    }
}

internal fun MasterContactsFragment.navigateToContactEditor(sipUriToAdd: String? = null) {
    val bundle = if (sipUriToAdd != null) bundleOf("SipUri" to sipUriToAdd) else Bundle()
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.masterContactsFragment) {
            findNavController().navigate(
                    R.id.action_masterContactsFragment_to_contactEditorFragment,
                    bundle,
                    getRightToLeftAnimationNavOptions()
            )
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.contacts_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_contactEditorFragment,
                bundle,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun ContactEditorFragment.navigateToContact(contact: NativeContact) {
    val bundle = Bundle()
    bundle.putString("id", contact.nativeId)
    findNavController().navigate(
            R.id.action_contactEditorFragment_to_detailContactFragment,
            bundle,
            getRightToLeftAnimationNavOptions(R.id.masterContactsFragment, false)
    )
}

internal fun DetailContactFragment.navigateToChatRoom(args: Bundle?) {
    if (!resources.getBoolean(R.bool.isTablet)) {
        findNavController().navigate(
                R.id.action_detailContactFragment_to_detailChatRoomFragment,
                args,
                getRightBottomToLeftTopAnimationNavOptions()
        )
    } else {
        findMasterNavController().navigate(
                R.id.action_global_masterChatRoomsFragment,
                args,
                getRightBottomToLeftTopAnimationNavOptions()
        )
    }
}

internal fun DetailContactFragment.navigateToDialer(args: Bundle?) {
    findMasterNavController().navigate(
            R.id.action_global_dialerFragment,
            args,
            getRightToLeftAnimationNavOptions()
    )
}

internal fun DetailContactFragment.navigateToContactEditor() {
    if (findNavController().currentDestination?.id == R.id.detailContactFragment) {
        findNavController().navigate(
                R.id.action_detailContactFragment_to_contactEditorFragment,
                null,
                getRightToLeftAnimationNavOptions()
        )
    }
}

*/
/* History related *//*


internal fun MasterCallLogsFragment.navigateToCallHistory() {
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.masterCallLogsFragment) {
            findNavController().navigate(
                    R.id.action_masterCallLogsFragment_to_detailCallLogFragment,
                    null,
                    getRightToLeftAnimationNavOptions()
            )
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.history_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_detailCallLogFragment,
                null,
                getRightToLeftAnimationNavOptions(R.id.emptyFragment, true)
        )
    }
}

internal fun MasterCallLogsFragment.navigateToDialer(args: Bundle?) {
    findNavController().navigate(
            R.id.action_global_dialerFragment,
            args,
            getRightToLeftAnimationNavOptions()
    )
}

internal fun DetailCallLogFragment.navigateToContacts(sipUriToAdd: String) {
    val deepLink = "linphone-android://contact/new/$sipUriToAdd"
    findMasterNavController().navigate(Uri.parse(deepLink), getRightToLeftAnimationNavOptions())
}

internal fun DetailCallLogFragment.navigateToContact(contact: NativeContact) {
    if (!resources.getBoolean(R.bool.isTablet)) {
        val args = Bundle()
        args.putString("id", contact.nativeId)
        findMasterNavController().navigate(
                R.id.action_detailCallLogFragment_to_detailContactFragment,
                args,
                getRightBottomToLeftTopAnimationNavOptions()
        )
    } else {
        val deepLink = "linphone-android://contact/view/${contact.nativeId}"
        findMasterNavController().navigate(Uri.parse(deepLink), getRightBottomToLeftTopAnimationNavOptions())
    }
}

internal fun DetailCallLogFragment.navigateToFriend(friendAddress: Address) {
    val deepLink = "linphone-android://contact/new/${friendAddress.asStringUriOnly()}"
    findMasterNavController().navigate(Uri.parse(deepLink), getRightToLeftAnimationNavOptions())
}

internal fun DetailCallLogFragment.navigateToChatRoom(args: Bundle?) {
    if (!resources.getBoolean(R.bool.isTablet)) {
        findNavController().navigate(
                R.id.action_detailCallLogFragment_to_detailChatRoomFragment,
                args,
                getRightBottomToLeftTopAnimationNavOptions()
        )
    } else {
        findMasterNavController().navigate(
                R.id.action_global_masterChatRoomsFragment,
                args,
                getRightBottomToLeftTopAnimationNavOptions()
        )
    }
}

internal fun DetailCallLogFragment.navigateToDialer(args: Bundle?) {
    findMasterNavController().navigate(
            R.id.action_global_dialerFragment,
            args,
            getRightToLeftAnimationNavOptions()
    )
}

*/
/* Settings related *//*


internal fun SettingsFragment.navigateToAccountSettings(identity: String) {
    val bundle = bundleOf("Identity" to identity)
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            findNavController().navigate(
                    R.id.action_settingsFragment_to_accountSettingsFragment,
                    bundle,
                    getRightToLeftAnimationNavOptions()
            )
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.settings_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_accountSettingsFragment,
                bundle,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun SettingsFragment.navigateToTunnelSettings() {
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            findNavController().navigate(
                    R.id.action_settingsFragment_to_tunnelSettingsFragment,
                    null,
                    getRightToLeftAnimationNavOptions()
            )
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.settings_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_tunnelSettingsFragment,
                null,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun SettingsFragment.navigateToAudioSettings() {
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            findNavController().navigate(
                    R.id.action_settingsFragment_to_audioSettingsFragment,
                    null,
                    getRightToLeftAnimationNavOptions()
            )
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.settings_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_audioSettingsFragment,
                null,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun SettingsFragment.navigateToVideoSettings() {
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            findNavController().navigate(
                    R.id.action_settingsFragment_to_videoSettingsFragment,
                    null,
                    getRightToLeftAnimationNavOptions()
            )
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.settings_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_videoSettingsFragment,
                null,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun SettingsFragment.navigateToCallSettings() {
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            findNavController().navigate(
                    R.id.action_settingsFragment_to_callSettingsFragment,
                    null,
                    getRightToLeftAnimationNavOptions()
            )
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.settings_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_callSettingsFragment,
                null,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun SettingsFragment.navigateToChatSettings() {
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            findNavController().navigate(
                    R.id.action_settingsFragment_to_chatSettingsFragment,
                    null,
                    getRightToLeftAnimationNavOptions()
            )
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.settings_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_chatSettingsFragment,
                null,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun SettingsFragment.navigateToNetworkSettings() {
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            findNavController().navigate(
                    R.id.action_settingsFragment_to_networkSettingsFragment,
                    null,
                    getRightToLeftAnimationNavOptions()
            )
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.settings_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_networkSettingsFragment,
                null,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun SettingsFragment.navigateToContactsSettings() {
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            findNavController().navigate(
                    R.id.action_settingsFragment_to_contactsSettingsFragment,
                    null,
                    getRightToLeftAnimationNavOptions()
            )
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.settings_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_contactsSettingsFragment,
                null,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun SettingsFragment.navigateToAdvancedSettings() {
    if (!resources.getBoolean(R.bool.isTablet)) {
        if (findNavController().currentDestination?.id == R.id.settingsFragment) {
            findNavController().navigate(
                    R.id.action_settingsFragment_to_advancedSettingsFragment,
                    null,
                    getRightToLeftAnimationNavOptions()
            )
        }
    } else {
        val navHostFragment =
                childFragmentManager.findFragmentById(R.id.settings_nav_container) as NavHostFragment
        navHostFragment.navController.navigate(
                R.id.action_global_advancedSettingsFragment,
                null,
                getRightToLeftAnimationNavOptions()
        )
    }
}

internal fun AccountSettingsFragment.navigateToPhoneLinking(args: Bundle?) {
    if (findNavController().currentDestination?.id == R.id.accountSettingsFragment) {
        findNavController().navigate(
                R.id.action_accountSettingsFragment_to_phoneAccountLinkingFragment,
                args,
                getRightToLeftAnimationNavOptions()
        )
    }
}

*/
/* Side menu related *//*


internal fun SideMenuFragment.navigateToAccountSettings(identity: String) {
    val deepLink = if (!resources.getBoolean(R.bool.isTablet)) {
        // If not a tablet, navigate directly to account settings fragment
        "linphone-android://account-settings/$identity"
    } else {
        // On tablet, to keep the categories list on left side, navigate to settings fragment first
        "linphone-android://settings/$identity"
    }
    findNavController().navigate(Uri.parse(deepLink), getRightToLeftAnimationNavOptions())
}

internal fun SideMenuFragment.navigateToSettings() {
    findNavController().navigate(
            R.id.action_global_settingsFragment,
            null,
            getRightToLeftAnimationNavOptions(R.id.settingsFragment)
    )
}

internal fun SideMenuFragment.navigateToAbout() {
    findNavController().navigate(
            R.id.action_global_aboutFragment,
            null,
            getRightToLeftAnimationNavOptions(R.id.aboutFragment)
    )
}

internal fun SideMenuFragment.navigateToRecordings() {
    findNavController().navigate(
            R.id.action_global_recordingsFragment,
            null,
            getRightToLeftAnimationNavOptions(R.id.recordingsFragment)
    )
}
*/
