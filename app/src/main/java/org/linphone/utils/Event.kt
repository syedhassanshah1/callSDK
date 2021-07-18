
package org.linphone.utils

import java.util.concurrent.atomic.AtomicBoolean

/**
 * This class allows to limit the number of notification for an event.
 * The first one to consume the event will stop the dispatch.
 */
open class Event<out T>(private val content: T) {
    private val handled = AtomicBoolean(false)

    fun consumed(): Boolean {
        return handled.get()
    }

    fun consume(handleContent: (T) -> Unit) {
        if (!handled.get()) {
            handled.set(true)
            handleContent(content)
        }
    }
}
