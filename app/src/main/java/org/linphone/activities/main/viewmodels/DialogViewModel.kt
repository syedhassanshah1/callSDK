
package org.linphone.activities.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.linphone.R
import org.linphone.utils.AppUtils

class DialogViewModel(val message: String, val title: String = "") : ViewModel() {
    var showDoNotAskAgain: Boolean = false

    var showZrtp: Boolean = false

    var zrtpReadSas: String = ""

    var zrtpListenSas: String = ""

    var showTitle: Boolean = false

    var showIcon: Boolean = false

    var iconResource: Int = 0

    val doNotAskAgain = MutableLiveData<Boolean>()

    init {
        doNotAskAgain.value = false
        showTitle = title.isNotEmpty()
    }

    var showCancel: Boolean = false
    var cancelLabel: String = AppUtils.getString(R.string.dialog_cancel)
    private var onCancel: (Boolean) -> Unit = {}

    fun showCancelButton(cancel: (Boolean) -> Unit) {
        showCancel = true
        onCancel = cancel
    }

    fun showCancelButton(cancel: (Boolean) -> Unit, label: String = cancelLabel) {
        showCancel = true
        onCancel = cancel
        cancelLabel = label
    }

    fun onCancelClicked() {
        onCancel(doNotAskAgain.value == true)
    }

    var showDelete: Boolean = false
    var deleteLabel: String = AppUtils.getString(R.string.dialog_delete)
    private var onDelete: (Boolean) -> Unit = {}

    fun showDeleteButton(delete: (Boolean) -> Unit, label: String) {
        showDelete = true
        onDelete = delete
        deleteLabel = label
    }

    fun onDeleteClicked() {
        onDelete(doNotAskAgain.value == true)
    }

    var showOk: Boolean = false
    var okLabel: String = AppUtils.getString(R.string.dialog_ok)
    private var onOk: (Boolean) -> Unit = {}

    fun showOkButton(ok: (Boolean) -> Unit, label: String = okLabel) {
        showOk = true
        onOk = ok
        okLabel = label
    }

    fun onOkClicked() {
        onOk(doNotAskAgain.value == true)
    }
}
