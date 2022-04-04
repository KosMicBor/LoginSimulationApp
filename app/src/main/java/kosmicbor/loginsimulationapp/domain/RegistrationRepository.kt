package kosmicbor.loginsimulationapp.domain

interface RegistrationRepository {

    fun addNewUserRequest(
        nickname: String,
        login: String,
        password: String,
        onUserCreateListener: DatabaseRepository.OnUserCreateListener
    )
}