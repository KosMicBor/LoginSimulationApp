package kosmicbor.loginsimulationapp

import android.os.Handler
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

class RegistrationPresenterTest {

    private val nickname = "nickname"

    private val login = "user"

    private val password = "123"

    @Mock
    private lateinit var registrationInteractor: RegistrationInteractor

    @Mock
    private lateinit var handler: Handler

    @Mock
    private lateinit var view: RegistrationContract.RegistrationView

//    @Mock
//    private lateinit var registrationCallback: RegistrationInteractorImpl.RegisterNewUserCallback

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
    fun registrationPresenter_OnRegistration_FieldsNotEmpty_Test() {

        registrationPresenter.onRegistration(
            nickname = nickname,
            newLogin = login,
            newPassword = password,
            newPasswordRepeat = password
        )

        //verify(view, times(1)).showProgress()

        verify(registrationInteractor, times(1)).registerNewUser(
            nickname = anyString(),
            newLogin = anyString(),
            newPassword = anyString(),
            callback = any()
        )

        //verify(view, times(1)).setSuccess()
    }

    @Test
    fun registrationPresenter_OnRegistration_AllFieldsIsEmpty_Test() {

        registrationPresenter.onRegistration(
            nickname = "",
            newLogin = "",
            newPassword = "",
            newPasswordRepeat = ""
        )

        verify(view, times(1)).showProgress()


        verify(view, times(1)).showStandardScreen()
        verify(view, times(1)).setError(anyString())
    }

    @Test
    fun registrationPresenter_OnRegistration_SomeFieldIsEmpty_Test() {

        registrationPresenter.onRegistration(
            nickname = nickname,
            newLogin = "",
            newPassword = password,
            newPasswordRepeat = ""
        )

        verify(view, times(1)).showStandardScreen()
        verify(view, times(1)).setError(anyString())
    }
}