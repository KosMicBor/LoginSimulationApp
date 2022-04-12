package kosmicbor.loginsimulationapp.domain

import kosmicbor.loginsimulationapp.data.LoginInteractorImpl

interface LoginInteractor {
    fun logIn(
        login: String,
        password: String,
        callback: LoginInteractorImpl.LoginCallback
    )

    fun changePassword(
        login: String,
        newPassword: String,
        callback: LoginInteractorImpl.ChangePasswordCallback
    )

    fun verifyEmail(loginEmail: String, callback: LoginInteractorImpl.VerifyEmailCallback)
}