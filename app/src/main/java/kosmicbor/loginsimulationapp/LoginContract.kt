package kosmicbor.loginsimulationapp

import kosmicbor.loginsimulationapp.domain.DatabaseRepository

class LoginContract {

    interface Presenter {
        fun onAttach(loginView: LoginView)
        fun onLogIn(login: String, password: String)
        fun onPasswordChanged(login: String, newPassword: String)
        fun onVerifyEmail(loginEmail: String)
    }

    interface LoginView {
        fun setSuccess()
        fun setError(error: String)
        fun showProgress()
        fun showStandardScreen()
        fun showPasswordChangeDialog()
        fun enableDialogPasswordChangeFields()
        fun showMessage(message: String)
    }
}