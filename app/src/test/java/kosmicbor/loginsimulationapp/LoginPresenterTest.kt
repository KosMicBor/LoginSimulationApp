package kosmicbor.loginsimulationapp

import android.os.Handler
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
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

    private val newPassword = "new password"

    @Mock
    private lateinit var loginInteractor: LoginInteractor

    @Mock
    private lateinit var handler: Handler

    @Mock
    private lateinit var view: LoginContract.LoginView

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

        whenever(
            loginInteractor.logIn(
                login = anyString(),
                password = anyString(),
                callback = any()
            )
        ).thenAnswer {
            val answer = it.arguments[2] as LoginInteractorImpl.LoginCallback
            return@thenAnswer answer.onSuccess()
        }

        loginPresenter.onLogIn(login, password)

        verify(view).showStandardScreen()
        verify(view).setSuccess()
        assertTrue(loginPresenter.isSuccess)
    }

    @Test
    fun loginPresenter_OnLogIn_Response_Loading_Test() {

        whenever(
            loginInteractor.logIn(
                login = anyString(),
                password = anyString(),
                callback = any()
            )
        ).thenAnswer {
            val answer = it.arguments[2] as LoginInteractorImpl.LoginCallback
            return@thenAnswer answer.onLoading()
        }

        loginPresenter.onLogIn(login, password)

        verify(view).showProgress()
    }

    @Test
    fun loginPresenter_OnLogIn_Response_Error_Test() {

        whenever(
            loginInteractor.logIn(
                login = anyString(),
                password = anyString(),
                callback = any()
            )
        ).thenAnswer {
            val answer = it.arguments[2] as LoginInteractorImpl.LoginCallback
            return@thenAnswer answer.onError("Error")
        }

        loginPresenter.onLogIn(login, password)

        verify(view).showStandardScreen()
        verify(view).setError(anyString())
        assertFalse(loginPresenter.isSuccess)
    }

    @Test
    fun loginPresenter_OnPasswordChanged_Test(){
        loginPresenter.onPasswordChanged(password, newPassword)

        verify(loginInteractor, times(1)).changePassword(
            login = anyString(),
            newPassword = anyString(),
            callback = any()
        )
    }

    @Test
    fun loginPresenter_OnVerifyEmail_Test(){
        loginPresenter.onVerifyEmail(login)

        verify(loginInteractor, times(1)).verifyEmail(
            loginEmail = anyString(),
            callback = any()
        )
    }
}