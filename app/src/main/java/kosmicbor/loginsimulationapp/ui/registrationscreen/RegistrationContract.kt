package kosmicbor.loginsimulationapp.ui.registrationscreen

class RegistrationContract {

    interface RegistrationPresenter {
        fun onAttach(registrationView: RegistrationView)
        fun onRegistration(
            nickname: String,
            newLogin: String,
            newPassword: String,
            newPasswordRepeat: String
        )

        fun checkPasswordsConformity(newPassword: String, newPasswordRepeat: String): Boolean
    }

    interface RegistrationView {
        fun setSuccess()
        fun setError(error: String)
        fun showProgress()
        fun showStandardScreen()
    }
}