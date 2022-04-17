package kosmicbor.loginsimulationapp.utils

import android.os.Handler

class Publisher<T>(private val isOneTimeCommand: Boolean = false) {

    private var value: T? = null
    private var isCommandCompleted = false
    private val subscribers: MutableSet<Subscriber<T?>> = mutableSetOf()

    fun subscribe(handler: Handler, callback: (T?) -> Unit) {

        val subscriber = Subscriber(handler, callback)
        subscribers.add(subscriber)
    }

    fun unsubscribeAll() {
        subscribers.clear()
    }

    fun post(value: T?) {

        if (isOneTimeCommand) {
            this.value = value
            isCommandCompleted = true
        }

        subscribers.forEach { subscriber ->

            subscriber.invoke(value)

        }
    }
}