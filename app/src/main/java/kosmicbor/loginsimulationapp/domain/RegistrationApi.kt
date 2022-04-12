package kosmicbor.loginsimulationapp.domain

import kosmicbor.loginsimulationapp.data.MockDatabaseApiImpl

interface RegistrationApi {

    fun addNewUserRequest(
        nickname: String,
        login: String,
        password: String,
        onUserCreateListener: MockDatabaseApiImpl.OnUserCreateListener
    )
}