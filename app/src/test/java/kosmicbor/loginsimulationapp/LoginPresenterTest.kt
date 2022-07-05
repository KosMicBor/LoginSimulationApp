package kosmicbor.loginsimulationapp

import android.os.Handler
import junit.framework.Assert.assertFalse
import kosmicbor.loginsimulationapp.data.LoginInteractorImpl
import kosmicbor.loginsimulationapp.domain.LoginInteractor
import kosmicbor.loginsimulationapp.ui.loginscreen.LoginContract
import kosmicbor.loginsimulationapp.ui.loginscreen.LoginPresenter
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class LoginPresenterTest {

    private val login = "user"

    private val password = "password"

    @Mock
    private lateinit var loginInteractor: LoginInteractor

    @Mock
    private lateinit var handler: Handler

    @Mock
    private lateinit var view: LoginContract.LoginView

    @Mock
    private lateinit var loginCallback: LoginInteractorImpl.LoginCallback

    private lateinit var loginPresenter: LoginPresenter

    @Before
    fun setUp() {

        MockitoAnnotations.openMocks(this)

        loginPresenter = LoginPresenter(loginInteractor, handler)

        loginPresenter.onAttach(view)
    }

    @Test
    fun loginPresenter_OnLogIn_Test() {

        loginPresenter.onLogIn(login, password)

        verify(loginInteractor, times(1)).logIn(
            login = anyString(),
            password = anyString(),
            callback = any()
        )
    }

    @Test
    fun loginPresenter_OnLogIn_Response_Success_Test() {

        loginPresenter.onLogIn(login, password)

        whenever(
            loginInteractor.logIn(
                anyString(),
                anyString(),
                any()
            )
        ).thenAnswer {
            val answer = it.arguments[0] as LoginInteractorImpl.LoginCallback
            return@thenAnswer answer.onSuccess()
        }

        verify(view).setSuccess()
    }

    @Test
    fun loginPresenter_OnLogIn_Response_Loading_Test() {

        loginPresenter.onLogIn(login, password)

        `when`(loginCallback.onLoading()).then {
            verify(view, times(1)).showProgress()
        }
    }

    @Test
    fun loginPresenter_OnLogIn_Response_Error_Test() {

        loginPresenter.onLogIn(login, password)

        `when`(loginCallback.onError(anyString())).then {
            verify(view, times(1)).showStandardScreen()
            verify(view, times(1)).setError(anyString())
            assertFalse("Successful state must became false", loginPresenter.isSuccess)
        }
    }
}