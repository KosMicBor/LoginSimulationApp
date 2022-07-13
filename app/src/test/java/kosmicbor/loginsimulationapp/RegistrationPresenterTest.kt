package kosmicbor.loginsimulationapp

import android.os.Handler
import junit.framework.Assert.assertFalse
import kosmicbor.loginsimulationapp.data.RegistrationInteractorImpl
import kosmicbor.loginsimulationapp.domain.RegistrationInteractor
import kosmicbor.loginsimulationapp.ui.registrationscreen.RegistrationContract
import kosmicbor.loginsimulationapp.ui.registrationscreen.RegistrationPresenter
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class RegistrationPresenterTest {

    private val nickname = "nickname"

    private val login = "user"

    private val password = "123"
    private val anotherPassword = "another_password"

    @Mock
    private lateinit var registrationInteractor: RegistrationInteractor

    @Mock
    private lateinit var handler: Handler

    @Mock
    private lateinit var view: RegistrationContract.RegistrationView

    private lateinit var registrationPresenter: RegistrationPresenter

    @Before
    fun setUp() {

        MockitoAnnotations.openMocks(this)

        `when`(handler.post(any(Runnable::class.java))).thenAnswer {

            (it.arguments[0] as? Runnable?)?.run()

            true
        }

        registrationPresenter = RegistrationPresenter(registrationInteractor, handler)

        registrationPresenter.onAttach(view)
    }

    @Test
    fun registrationPresenter_OnRegistration_Test() {
        registrationPresenter.onRegistration(
            nickname = nickname,
            newLogin = login,
            newPassword = password,
            newPasswordRepeat = password
        )

        verify(registrationInteractor, times(1)).registerNewUser(
            nickname = anyString(),
            newLogin = anyString(),
            newPassword = anyString(),
            callback = any()
        )
    }

    @Test
    fun registrationPresenter_OnRegistration_FieldsNotEmpty_onSuccess_Test() {

        whenever(
            registrationInteractor.registerNewUser(
                nickname = anyString().ifEmpty { "nickname" },
                newLogin = anyString().ifEmpty { "login" },
                newPassword = anyString().ifEmpty { "password" },
                callback = any()
            )
        ).thenAnswer {
            val answer = it.arguments[3] as RegistrationInteractorImpl.RegisterNewUserCallback
            return@thenAnswer answer.onSuccess()
        }

        registrationPresenter.onRegistration(
            nickname = nickname,
            newLogin = login,
            newPassword = password,
            newPasswordRepeat = password
        )

        verify(registrationInteractor, times(1)).registerNewUser(
            nickname = anyString(),
            newLogin = anyString(),
            newPassword = anyString(),
            callback = any()
        )

        verify(view, times(1)).showStandardScreen()
        verify(view, times(1)).setSuccess()
    }

    @Test
    fun registrationPresenter_OnRegistration_AllFieldsIsEmpty_Test() {

        whenever(
            registrationInteractor.registerNewUser(
                nickname = anyString(),
                newLogin = anyString(),
                newPassword = anyString(),
                callback = any()
            )
        ).thenAnswer {
            val answer = it.arguments[3] as RegistrationInteractorImpl.RegisterNewUserCallback
            return@thenAnswer answer.onSuccess()
        }

        registrationPresenter.onRegistration(
            nickname = "",
            newLogin = "",
            newPassword = "",
            newPasswordRepeat = ""
        )

        verify(view, times(1)).showStandardScreen()
        verify(view, times(1)).setError(anyString())
    }

    @Test
    fun registrationPresenter_OnRegistration_SomeFieldIsEmpty_Test() {

        whenever(
            registrationInteractor.registerNewUser(
                nickname = anyString(),
                newLogin = anyString(),
                newPassword = anyString(),
                callback = any()
            )
        ).thenAnswer {
            val answer = it.arguments[3] as RegistrationInteractorImpl.RegisterNewUserCallback
            return@thenAnswer answer.onSuccess()
        }

        registrationPresenter.onRegistration(
            nickname = nickname,
            newLogin = "",
            newPassword = password,
            newPasswordRepeat = password
        )

        verify(view, times(1)).showStandardScreen()
        verify(view, times(1)).setError(anyString())
    }

    @Test
    fun registrationPresenter_OnRegistration_IfPasswordsNotMatch_Test() {

        whenever(
            registrationInteractor.registerNewUser(
                nickname = anyString(),
                newLogin = anyString(),
                newPassword = anyString(),
                callback = any()
            )
        ).thenAnswer {
            val answer = it.arguments[3] as RegistrationInteractorImpl.RegisterNewUserCallback
            return@thenAnswer answer.onSuccess()
        }

        registrationPresenter.onRegistration(
            nickname = nickname,
            newLogin = login,
            newPassword = password,
            newPasswordRepeat = anotherPassword
        )

        verify(view, times(1)).showStandardScreen()
        verify(view, times(1)).setError(anyString())
        assertFalse(registrationPresenter.checkPasswordsConformity(password, anotherPassword))
    }

    @Test
    fun registrationPresenter_OnRegistration_onLoading_Test() {

        whenever(
            registrationInteractor.registerNewUser(
                nickname = anyString().ifEmpty { "nickname" },
                newLogin = anyString().ifEmpty { "login" },
                newPassword = anyString().ifEmpty { "password" },
                callback = any()
            )
        ).thenAnswer {
            val answer = it.arguments[3] as RegistrationInteractorImpl.RegisterNewUserCallback
            return@thenAnswer answer.onLoading()
        }

        registrationPresenter.onRegistration(
            nickname = nickname,
            newLogin = login,
            newPassword = password,
            newPasswordRepeat = password
        )

        verify(registrationInteractor, times(1)).registerNewUser(
            nickname = anyString(),
            newLogin = anyString(),
            newPassword = anyString(),
            callback = any()
        )

        verify(view, times(1)).showProgress()
    }

    @Test
    fun registrationPresenter_OnRegistration_onError_Test() {

        whenever(
            registrationInteractor.registerNewUser(
                nickname = anyString().ifEmpty { "nickname" },
                newLogin = anyString().ifEmpty { "login" },
                newPassword = anyString().ifEmpty { "password" },
                callback = any()
            )
        ).thenAnswer {
            val answer = it.arguments[3] as RegistrationInteractorImpl.RegisterNewUserCallback
            return@thenAnswer answer.onError("Error")
        }

        registrationPresenter.onRegistration(
            nickname = nickname,
            newLogin = login,
            newPassword = password,
            newPasswordRepeat = password
        )

        verify(registrationInteractor, times(1)).registerNewUser(
            nickname = anyString(),
            newLogin = anyString(),
            newPassword = anyString(),
            callback = any()
        )

        verify(view, times(1)).showStandardScreen()
        verify(view, times(1)).setError(anyString())
    }
}