package kosmicbor.loginsimulationapp.data

interface LoginApi {
    fun checkUserCredentialsRequest(
        login: String,
        password: String,
        onUserLogInListener: DatabaseApi.OnUserLogInListener
    )

    fun changeUserPasswordRequest(
        login: String,
        newPassword: String,
        onChangePasswordListener: DatabaseApi.OnChangePasswordListener
    )

    fun verifyEmail(
        loginEmail: String,
        onVerifyEmailListener: DatabaseApi.OnVerifyEmailListener
    )
}