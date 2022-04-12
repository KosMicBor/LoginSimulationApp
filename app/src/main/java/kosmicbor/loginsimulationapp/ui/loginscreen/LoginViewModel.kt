package kosmicbor.loginsimulationapp.ui.loginscreen

import androidx.annotation.MainThread
import kosmicbor.loginsimulationapp.data.LoginInteractorImpl
import kosmicbor.loginsimulationapp.domain.LoginInteractor
import kosmicbor.loginsimulationapp.utils.Publisher

class LoginViewModel(
    private val interactor: LoginInteractor,
) : LoginContract.LoginViewModel {

    override val isLoginSuccess: Publisher<Boolean> = Publisher()
    override val isChangePasswordSuccess: Publisher<String?> = Publisher(true)
    override val isVerifyEmailSuccess: Publisher<String?> = Publisher(true)
    override val isInProgress: Publisher<Boolean> = Publisher()
    override val isVerifyEmailInProgress: Publisher<Boolean> = Publisher()
    override val isVerifyEmailError: Publisher<String?> = Publisher(true)
    override val isError: Publisher<String?> = Publisher(true)

    override fun logIn(login: String, password: String) {
        interactor.logIn(login, password, object : LoginInteractorImpl.LoginCallback {

            override fun onSuccess() {
                isLoginSuccess.post(true)
            }

            override fun onError(error: String) {
                isError.post(error)
            }

            override fun onLoading() {
                isInProgress.post(true)
            }
        })
    }

    override fun changePassword(login: String, newPassword: String) {
        interactor.changePassword(
            login,
            newPassword,
            object : LoginInteractorImpl.ChangePasswordCallback {
                override fun onSuccess(message: String) {
                    isChangePasswordSuccess.post(message)
                }

                override fun onError(error: String) {
                    isError.post(error)
                }

                override fun onLoading() {
                    isInProgress.post(true)
                }
            })
    }

    @MainThread
    override fun verifyEmail(loginEmail: String) {

        interactor.verifyEmail(loginEmail, object : LoginInteractorImpl.VerifyEmailCallback {

            override fun onSuccess(message: String) {
                isVerifyEmailSuccess.post(message)
            }

            override fun onError(error: String) {
                isVerifyEmailError.post(error)
            }

            override fun onLoading() {
                isVerifyEmailInProgress.post(true)
            }
        })
    }
}