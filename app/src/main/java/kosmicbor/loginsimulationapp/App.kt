package kosmicbor.loginsimulationapp

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import kosmicbor.loginsimulationapp.data.MockDatabaseApiImpl
import kosmicbor.loginsimulationapp.data.LoginInteractorImpl
import kosmicbor.loginsimulationapp.domain.LoginInteractor

class App : Application() {
    private val databaseApi: MockDatabaseApiImpl by lazy {
        MockDatabaseApiImpl()
    }
    val loginInteractor: LoginInteractor by lazy {
        LoginInteractorImpl(
            app.databaseApi,
            Handler(Looper.myLooper() ?: Looper.getMainLooper())
        )
    }
}

val Context.app: App
    get() {
        return applicationContext as App
    }