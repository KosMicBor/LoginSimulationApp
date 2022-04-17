package kosmicbor.loginsimulationapp.data

import android.os.Handler
import kosmicbor.loginsimulationapp.domain.RegistrationInteractor

class RegistrationInteractorImpl(
    private val databaseApiImpl: MockDatabaseApiImpl,
) : RegistrationInteractor {

    companion object {
        private const val REGISTRATION_REQUEST_DELAY = 1000L
    }

    override fun registerNewUser(
        nickname: String,
        newLogin: String,
        newPassword: String,
        callback: RegisterNewUserCallback
    ) {
        Thread {

            callback.onLoading()

            Thread.sleep(REGISTRATION_REQUEST_DELAY)

            databaseApiImpl.addNewUserRequest(nickname, newLogin, newPassword, object :
                MockDatabaseApiImpl.OnUserCreateListener {

                override fun createSuccess() {
                    callback.onSuccess()
                }

                override fun createError(error: String) {
                    callback.onError(error)
                }

            })


        }.start()
    }

    interface RegisterNewUserCallback {
        fun onSuccess()
        fun onError(error: String)
        fun onLoading()
    }
}