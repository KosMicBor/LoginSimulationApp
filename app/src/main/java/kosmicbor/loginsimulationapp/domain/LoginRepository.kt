package kosmicbor.loginsimulationapp.domain

interface LoginRepository {
    fun checkUserCredentialsRequest(
        login: String,
        password: String,
        onUserLogInListener: DatabaseRepository.OnUserLogInListener
    )

    fun changeUserPasswordRequest(
        login: String,
        newPassword: String,
        onChangePasswordListener: DatabaseRepository.OnChangePasswordListener
    )

    fun verifyEmail(
        loginEmail: String,
        onVerifyEmailListener: DatabaseRepository.OnVerifyEmailListener
    )
}