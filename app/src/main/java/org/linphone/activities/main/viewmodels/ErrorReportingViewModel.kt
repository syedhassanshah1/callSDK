
package org.linphone.activities.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.linphone.utils.Event

/* Helper for view models to notify user of an error through a Snackbar */
abstract class ErrorReportingViewModel : ViewModel() {
    val onErrorEvent = MutableLiveData<Event<Int>>()
}
