package kosmicbor.loginsimulationapp

import kosmicbor.loginsimulationapp.domain.DatabaseRepository

class RegistrationContract {

    interface RegistrationPresenter {
        fun onAttach(RegistrationView: RegistrationView)
        fun onRegistration(
            nickname: String,
            newLogin: String,
            newPassword: String,
            newPasswordRepeat: String
        )
    }

    interface RegistrationView {
        fun setSuccess()
        fun setError(error: String)
        fun showProgress()
        fun showStandardScreen()
    }
}