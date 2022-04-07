package kosmicbor.loginsimulationapp.domain

import kosmicbor.loginsimulationapp.data.MockDatabaseApiImpl

interface LoginApi {
    fun checkUserCredentialsRequest(
        login: String,
        password: String,
        onUserLogInListener: MockDatabaseApiImpl.OnUserLogInListener
    )

    fun changeUserPasswordRequest(
        login: String,
        newPassword: String,
        onChangePasswordListener: MockDatabaseApiImpl.OnChangePasswordListener
    )

    fun verifyEmail(
        loginEmail: String,
        onVerifyEmailListener: MockDatabaseApiImpl.OnVerifyEmailListener
    )
}