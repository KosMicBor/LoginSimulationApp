package kosmicbor.loginsimulationapp.ui.loginscreen

import kosmicbor.loginsimulationapp.utils.Publisher

class LoginContract {

    interface LoginViewModel {
        val isInProgress: Publisher<Boolean>
        val isLoginSuccess: Publisher<Boolean>
        val isError: Publisher<String?>
        val isChangePasswordSuccess: Publisher<String?>
        val isVerifyEmailInProgress: Publisher<Boolean>
        val isVerifyEmailSuccess: Publisher<String?>
        val isVerifyEmailError: Publisher<String?>

        fun logIn(login: String, password: String)
        fun changePassword(login: String, newPassword: String)
        fun verifyEmail(loginEmail: String)
    }
}