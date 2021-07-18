
package org.linphone.utils

/**
 * Helper class to create singletons like CoreContext.
 */
open class SingletonHolder<out T : Any, in A>(val creator: (A) -> T) {
    @Volatile private var instance: T? = null

    fun exists(): Boolean {
        return instance != null
    }

    fun destroy() {
        instance = null
    }

    fun get(): T {
        // Will throw NPE if needed
        return instance!!
    }

    fun create(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator(arg)
                instance = created
                created
            }
        }
    }

    fun required(arg: A): T {
        return instance ?: create(arg)
    }
}
