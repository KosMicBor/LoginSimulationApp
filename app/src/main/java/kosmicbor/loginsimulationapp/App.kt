package kosmicbor.loginsimulationapp

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import kosmicbor.loginsimulationapp.data.MockDatabaseApiImpl
import kosmicbor.loginsimulationapp.data.DatabaseInterceptorImpl
import kosmicbor.loginsimulationapp.domain.DatabaseInterceptor

class App : Application() {
    private val databaseApi: MockDatabaseApiImpl by lazy {
        MockDatabaseApiImpl()
    }
    val databaseInterceptor: DatabaseInterceptor by lazy {
        DatabaseInterceptorImpl(
            app.databaseApi,
            Handler(Looper.myLooper() ?: Looper.getMainLooper())
        )
    }
}

val Context.app: App
    get() {
        return applicationContext as App
    }