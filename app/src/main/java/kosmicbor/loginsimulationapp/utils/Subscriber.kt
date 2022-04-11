package kosmicbor.loginsimulationapp.utils

import android.os.Handler


data class Subscriber<V>(
    private val handler: Handler,
    private val callback: (V?) -> Unit
) {
    fun invoke(value: V?) {
        handler.post {
            callback.invoke(value)
        }
    }

}
