package kosmicbor.loginsimulationapp

import android.app.Application
import android.content.Context
import kosmicbor.loginsimulationapp.data.LoginInteractorImpl
import kosmicbor.loginsimulationapp.data.MockDatabaseApiImpl
import kosmicbor.loginsimulationapp.data.RegistrationInteractorImpl
import kosmicbor.loginsimulationapp.domain.LoginInteractor
import kosmicbor.loginsimulationapp.domain.RegistrationInteractor

class App : Application() {
    private val databaseApi: MockDatabaseApiImpl by lazy {
        MockDatabaseApiImpl()
    }
    val loginInteractor: LoginInteractor by lazy {
        LoginInteractorImpl(
            app.databaseApi
        )
    }

    val registrationInteractor: RegistrationInteractor by lazy {
        RegistrationInteractorImpl(
            app.databaseApi
        )
    }
}

val Context.app: App
    get() {
        return applicationContext as App
    }