package kosmicbor.loginsimulationapp.data

import kosmicbor.loginsimulationapp.domain.User

object DatabaseApi : LoginApi, RegistrationApi {

    private val usersList: MutableList<User> = mutableListOf()

    override fun addNewUserRequest(
        nickname: String,
        login: String,
        password: String,
        onUserCreateListener: OnUserCreateListener
    ) {
        val newUser = User(nickname, login, password)
        usersList.forEach {
            if (it.userNickname == newUser.userNickname || it.userLogin == newUser.userLogin) {
                onUserCreateListener.createError("This nickname or login already exists")
                return
            }
        }

        usersList.add(newUser)
        onUserCreateListener.createSuccess()
    }

    override fun checkUserCredentialsRequest(
        login: String,
        password: String,
        onUserLogInListener: OnUserLogInListener
    ) {
        usersList.forEach {
            if (it.userLogin == login && it.userPassword == password) {
                onUserLogInListener.logInSuccess()
                return
            }
        }

        onUserLogInListener.logInError("Login or password is incorrect")
    }

    override fun changeUserPasswordRequest(
        login: String,
        newPassword: String,
        onChangePasswordListener: OnChangePasswordListener
    ) {
        usersList.forEach {
            if (it.userLogin == login) {
                it.userPassword = newPassword
                onChangePasswordListener.changeSuccess()
                return
            }
        }

        onChangePasswordListener.changeError("Can't change password, login not found")
    }

    override fun verifyEmail(loginEmail: String, onVerifyEmailListener: OnVerifyEmailListener) {
        usersList.forEach {
            if (it.userLogin == loginEmail) {
                onVerifyEmailListener.verifySuccess()
                return
            }
        }

        onVerifyEmailListener.verifyError("Email didn't find!")
    }

    interface OnUserCreateListener {
        fun createSuccess()
        fun createError(error: String)
    }

    interface OnUserLogInListener {
        fun logInSuccess()
        fun logInError(error: String)
    }

    interface OnChangePasswordListener {
        fun changeSuccess()
        fun changeError(error: String)
    }

    interface OnVerifyEmailListener {
        fun verifySuccess()
        fun verifyError(error: String)
    }
}
