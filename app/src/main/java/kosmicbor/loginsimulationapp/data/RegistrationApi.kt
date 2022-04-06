package kosmicbor.loginsimulationapp.data

interface RegistrationApi {

    fun addNewUserRequest(
        nickname: String,
        login: String,
        password: String,
        onUserCreateListener: DatabaseApi.OnUserCreateListener
    )
}