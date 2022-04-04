package kosmicbor.loginsimulationapp.presenters

import android.os.Handler
import android.os.Looper
import kosmicbor.loginsimulationapp.RegistrationContract
import kosmicbor.loginsimulationapp.domain.DatabaseRepository

class RegistrationPresenter : RegistrationContract.RegistrationPresenter {

    private val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())
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

        Thread {

            handler.post {
                regView?.showProgress()


                if (checkPasswordsConformity(newPassword, newPasswordRepeat)) {


                    if (nickname.isNotEmpty() && newLogin.isNotEmpty() && newPassword.isNotEmpty()) {
                        DatabaseRepository.addNewUserRequest(
                            nickname,
                            newLogin,
                            newPassword,
                            object : DatabaseRepository.OnUserCreateListener {
                                override fun createSuccess() {
                                    regView?.setSuccess()
                                }

                                override fun createError(error: String) {
                                    regView?.setError(error)
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
        }.start()
    }

    private fun checkPasswordsConformity(newPassword: String, newPasswordRepeat: String): Boolean {
        return newPassword == newPasswordRepeat
    }
}