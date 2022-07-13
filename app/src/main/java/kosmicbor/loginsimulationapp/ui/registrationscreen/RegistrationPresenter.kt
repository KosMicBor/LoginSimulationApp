package kosmicbor.loginsimulationapp.ui.registrationscreen

import android.os.Handler
import kosmicbor.loginsimulationapp.data.RegistrationInteractorImpl
import kosmicbor.loginsimulationapp.domain.RegistrationInteractor

class RegistrationPresenter(
    private val interactor: RegistrationInteractor,
    private val handler: Handler
) : RegistrationContract.RegistrationPresenter {

    private var regView: RegistrationContract.RegistrationView? = null
    private var isSuccess = false

    override fun onAttach(registrationView: RegistrationContract.RegistrationView) {
        this.regView = registrationView

        if (isSuccess) {
            regView?.setSuccess()
        }
    }

    override fun onRegistration(
        nickname: String,
        newLogin: String,
        newPassword: String,
        newPasswordRepeat: String
    ) {

        handler.post {

            if (checkPasswordsConformity(newPassword, newPasswordRepeat)) {

                if (nickname.isNotEmpty() && newLogin.isNotEmpty() && newPassword.isNotEmpty()) {
                    interactor.registerNewUser(
                        nickname,
                        newLogin,
                        newPassword,
                        object : RegistrationInteractorImpl.RegisterNewUserCallback {

                            override fun onSuccess() {
                                regView?.apply {
                                    showStandardScreen()
                                    setSuccess()
                                }
                            }

                            override fun onError(error: String) {
                                regView?.apply {
                                    showStandardScreen()
                                    setError(error)
                                }
                            }

                            override fun onLoading() {
                                regView?.showProgress()
                            }

                        })

                } else {
                    regView?.showStandardScreen()
                    regView?.setError("Please, fill all fields!")
                }

            } else {
                regView?.showStandardScreen()
                regView?.setError("Passwords don't match!")
            }
        }
    }

    override fun checkPasswordsConformity(newPassword: String, newPasswordRepeat: String): Boolean {
        return newPassword == newPasswordRepeat
    }
}