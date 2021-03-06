package kosmicbor.loginsimulationapp.data

import android.os.Handler
import android.util.Log
import kosmicbor.loginsimulationapp.domain.LoginInteractor

class LoginInteractorImpl(
    private val mockDatabaseApiImpl: MockDatabaseApiImpl,
) : LoginInteractor {

    companion object {
        private const val LOGIN_DELAY = 1000L
        private const val VERIFY_DELAY = 3000L
        private const val CHANGE_PASSWORD_DELAY = 1000L
    }

    override fun logIn(login: String, password: String, callback: LoginCallback) {

        Thread {

            callback.onLoading()

            Thread.sleep(LOGIN_DELAY)

            mockDatabaseApiImpl.checkUserCredentialsRequest(
                login,
                password,
                object : MockDatabaseApiImpl.OnUserLogInListener {
                    override fun logInSuccess() {
                        callback.onSuccess()
                    }

                    override fun logInError(error: String) {
                        callback.onError(error)
                    }
                })

        }.start()
    }

    override fun changePassword(
        login: String,
        newPassword: String,
        callback: ChangePasswordCallback
    ) {

        Thread {

            callback.onLoading()


            Thread.sleep(CHANGE_PASSWORD_DELAY)


            mockDatabaseApiImpl.changeUserPasswordRequest(
                login,
                newPassword,
                object : MockDatabaseApiImpl.OnChangePasswordListener {
                    override fun changeSuccess() {
                        callback.onSuccess("Password changed")
                    }

                    override fun changeError(error: String) {
                        callback.onError(error)
                    }
                })

        }.start()
    }

    override fun verifyEmail(loginEmail: String, callback: VerifyEmailCallback) {

        Thread {

            callback.onLoading()

            Thread.sleep(VERIFY_DELAY)

            mockDatabaseApiImpl.verifyEmail(loginEmail, object :
                MockDatabaseApiImpl.OnVerifyEmailListener {
                override fun verifySuccess() {

                    callback.onSuccess("Verify successful!")
                }

                override fun verifyError(error: String) {
                    callback.onError(error)
                }
            })

        }.start()
    }

    interface LoginCallback {
        fun onSuccess()
        fun onError(error: String)
        fun onLoading()
    }

    interface ChangePasswordCallback {
        fun onSuccess(message: String)
        fun onError(error: String)
        fun onLoading()
    }

    interface VerifyEmailCallback {
        fun onSuccess(message: String)
        fun onError(error: String)
        fun onLoading()
    }

}