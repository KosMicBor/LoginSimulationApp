package kosmicbor.loginsimulationapp.ui.loginscreen

import android.os.Handler
import android.os.Looper
import kosmicbor.loginsimulationapp.data.DatabaseApi

class LoginPresenter : LoginContract.Presenter {

    companion object {
        private const val LOGIN_DELAY = 1000L
        private const val VERIFY_DELAY = 1000L
    }

    private val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())
    private var loginView: LoginContract.LoginView? = null
    private var isSuccess = false

    override fun onAttach(loginView: LoginContract.LoginView) {
        this.loginView = loginView

        if (isSuccess) {
            handler.post {
                loginView.setSuccess()
            }
        }
    }

    override fun onLogIn(login: String, password: String) {
        Thread {
            handler.post {
                loginView?.showProgress()
            }
            Thread.sleep(LOGIN_DELAY)
            DatabaseApi.checkUserCredentialsRequest(
                login,
                password,
                object : DatabaseApi.OnUserLogInListener {
                    override fun logInSuccess() {

                        handler.post {
                            loginView?.setSuccess()
                        }

                        isSuccess = true
                    }

                    override fun logInError(error: String) {
                        handler.post {
                            loginView?.setError(error)
                        }
                    }

                })

        }.start()
    }

    override fun onPasswordChanged(login: String, newPassword: String) {
        Thread {
            handler.post {

                DatabaseApi.changeUserPasswordRequest(
                    login,
                    newPassword,
                    object : DatabaseApi.OnChangePasswordListener {
                        override fun changeSuccess() {
                            loginView?.showMessage("Password changed")
                        }

                        override fun changeError(error: String) {
                            loginView?.setError(error)
                        }

                    })
            }
        }.start()
    }

    override fun onVerifyEmail(loginEmail: String) {
        Thread {
            handler.post {
                loginView?.showProgress()
                Thread.sleep(VERIFY_DELAY)

                DatabaseApi.verifyEmail(loginEmail, object :
                    DatabaseApi.OnVerifyEmailListener {
                    override fun verifySuccess() {
                        loginView?.showStandardScreen()
                        loginView?.enableDialogPasswordChangeFields()

                    }

                    override fun verifyError(error: String) {
                        loginView?.setError(error)
                    }
                })
            }
        }.start()
    }
}