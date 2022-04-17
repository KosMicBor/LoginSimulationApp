package kosmicbor.loginsimulationapp.ui.registrationscreen

import kosmicbor.loginsimulationapp.utils.Publisher

class RegistrationContract {

    interface RegistrationViewModel {

        val isInProgress: Publisher<Boolean>
        val isRegistrationSuccess: Publisher<Boolean>
        val isError: Publisher<String?>

        fun onRegistration(
            nickname: String,
            newLogin: String,
            newPassword: String,
            newPasswordRepeat: String
        )
    }
}