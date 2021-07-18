
package org.linphone.activities.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.linphone.utils.Event

/**
 * This view model is dedicated to the top bar while in edition mode for item(s) selection in list
 */
class ListTopBarViewModel : ViewModel() {
    val isEditionEnabled = MutableLiveData<Boolean>()

    val isSelectionNotEmpty = MutableLiveData<Boolean>()

    val selectAllEvent: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    val unSelectAllEvent: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    val deleteSelectionEvent: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData<Event<Boolean>>()
    }

    val selectedItems = MutableLiveData<ArrayList<Int>>()

    init {
        isEditionEnabled.value = false
        isSelectionNotEmpty.value = false
        selectedItems.value = arrayListOf()
    }

    fun onSelectAll(lastIndex: Int) {
        val list = arrayListOf<Int>()
        list.addAll(0.rangeTo(lastIndex))

        selectedItems.value = list
        isSelectionNotEmpty.value = list.isNotEmpty()
    }

    fun onUnSelectAll() {
        val list = arrayListOf<Int>()

        selectedItems.value = list
        isSelectionNotEmpty.value = list.isNotEmpty()
    }

    fun onToggleSelect(position: Int) {
        val list = arrayListOf<Int>()
        list.addAll(selectedItems.value.orEmpty())

        if (list.contains(position)) {
            list.remove(position)
        } else {
            list.add(position)
        }

        isSelectionNotEmpty.value = list.isNotEmpty()
        selectedItems.value = list
    }
}
