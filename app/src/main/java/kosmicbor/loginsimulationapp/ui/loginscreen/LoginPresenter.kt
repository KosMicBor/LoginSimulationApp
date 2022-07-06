package kosmicbor.loginsimulationapp.ui.loginscreen

import android.os.Handler
import androidx.annotation.MainThread
import kosmicbor.loginsimulationapp.data.LoginInteractorImpl
import kosmicbor.loginsimulationapp.domain.LoginInteractor

class LoginPresenter(
    private val interactor: LoginInteractor,
    private val handler: Handler
) : LoginContract.Presenter {

    private var loginView: LoginContract.LoginView? = null
    var isSuccess = false
        private set

    override fun onAttach(loginView: LoginContract.LoginView) {
        this.loginView = loginView

        if (isSuccess) {
            handler.post {
                loginView.setSuccess()
            }
        }
    }

    override fun onLogIn(login: String, password: String) {
        interactor.logIn(login, password, object : LoginInteractorImpl.LoginCallback {

            override fun onSuccess() {
                loginView?.apply {
                    showStandardScreen()
                    setSuccess()
                    isSuccess = true
                }
            }

            override fun onError(error: String) {
                loginView?.apply {
                    showStandardScreen()
                    setError(error)
                    isSuccess = false
                }
            }

            override fun onLoading() {
                loginView?.showProgress()
            }
        })
    }

    override fun onPasswordChanged(login: String, newPassword: String) {

        interactor.changePassword(
            login,
            newPassword,
            object : LoginInteractorImpl.ChangePasswordCallback {
                override fun onSuccess(message: String) {
                    loginView?.apply {
                        showStandardScreen()
                        showMessage(message)
                    }
                }

                override fun onError(error: String) {
                    loginView?.apply {
                        showStandardScreen()
                        setError(error)
                    }
                }

                override fun onLoading() {
                    loginView?.showProgress()
                }

            })
    }

    @MainThread
    override fun onVerifyEmail(loginEmail: String) {

        interactor.verifyEmail(loginEmail, object : LoginInteractorImpl.VerifyEmailCallback {

            override fun onSuccess(message: String) {
                loginView?.apply {
                    showStandardScreen()
                    enableDialogPasswordChangeFields()
                }
            }

            override fun onError(error: String) {
                loginView?.setError(error)
            }

            override fun onLoading() {
                loginView?.showDialogVerifyProgress()
            }
        })
    }
}