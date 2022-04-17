package kosmicbor.loginsimulationapp.ui.registrationscreen

import kosmicbor.loginsimulationapp.data.RegistrationInteractorImpl
import kosmicbor.loginsimulationapp.domain.RegistrationInteractor
import kosmicbor.loginsimulationapp.utils.Publisher

class RegistrationViewModel(
    private val interactor: RegistrationInteractor
) : RegistrationContract.RegistrationViewModel {
    override val isInProgress: Publisher<Boolean> = Publisher()
    override val isRegistrationSuccess: Publisher<Boolean> = Publisher()
    override val isError: Publisher<String?> = Publisher(true)

    override fun onRegistration(
        nickname: String,
        newLogin: String,
        newPassword: String,
        newPasswordRepeat: String
    ) {

        if (checkPasswordsConformity(newPassword, newPasswordRepeat)) {

            if (nickname.isNotEmpty() && newLogin.isNotEmpty() && newPassword.isNotEmpty()) {
                interactor.registerNewUser(
                    nickname,
                    newLogin,
                    newPassword,
                    object : RegistrationInteractorImpl.RegisterNewUserCallback {

                        override fun onSuccess() {
                            isRegistrationSuccess.post(true)
                        }

                        override fun onError(error: String) {
                            isError.post(error)
                        }

                        override fun onLoading() {
                            isInProgress.post(true)
                        }

                    })

            } else {
                isError.post("Please, fill all fields!")
            }

        } else {
            isError.post("Passwords don't match!")
        }
    }

    private fun checkPasswordsConformity(newPassword: String, newPasswordRepeat: String): Boolean {
        return newPassword == newPasswordRepeat
    }
}