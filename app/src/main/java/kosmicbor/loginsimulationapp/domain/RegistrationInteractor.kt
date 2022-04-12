package kosmicbor.loginsimulationapp.domain

import kosmicbor.loginsimulationapp.data.RegistrationInteractorImpl

interface RegistrationInteractor {
    fun registerNewUser(nickname: String,
                        newLogin: String,
                        newPassword: String,
                        callback: RegistrationInteractorImpl.RegisterNewUserCallback
    )
}