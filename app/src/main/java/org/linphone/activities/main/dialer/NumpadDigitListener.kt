
package org.linphone.activities.main.dialer

interface NumpadDigitListener {
    fun handleClick(key: Char)
    fun handleLongClick(key: Char): Boolean
}
