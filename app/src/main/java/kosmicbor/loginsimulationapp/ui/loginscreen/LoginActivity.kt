package kosmicbor.loginsimulationapp.ui.loginscreen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kosmicbor.loginsimulationapp.R
import kosmicbor.loginsimulationapp.app
import kosmicbor.loginsimulationapp.databinding.ActivityLoginBinding
import kosmicbor.loginsimulationapp.ui.registrationscreen.RegisterActivity
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())
    private lateinit var dialog: Dialog
    private var verifySnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.bottomsheet_password_change)
        }

        viewModel = restoreViewModel()

        viewModel.isInProgress.subscribe(handler) {
            showProgress()
        }

        viewModel.isVerifyEmailInProgress.subscribe(handler) {
            showDialogVerifyProgress()
        }

        viewModel.isLoginSuccess.subscribe(handler) {
            showStandardScreen()
            setSuccess()
        }

        viewModel.isChangePasswordSuccess.subscribe(handler) {
            showStandardScreen()
            it?.let { it1 -> showMessage(it1) }
        }

        viewModel.isVerifyEmailSuccess.subscribe(handler) {
            enableDialogPasswordChangeFields()

        }

        viewModel.isError.subscribe(handler) {
            it?.let { it1 -> setError(it1) }
        }

        binding.loginButton.setOnClickListener {
            viewModel.logIn(
                binding.loginFieldEditText.text.toString(),
                binding.passwordFieldEditText.text.toString()
            )
        }

        binding.createAccountButton.setOnClickListener {
            openRegisterActivity()
        }

        binding.forgotPasswordButton.setOnClickListener {
            showPasswordChangeDialog()
        }
    }

    private fun openRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun setSuccess() {
        binding.loginScreenProgressbar.visibility = View.GONE
        binding.root.setBackgroundColor(Color.GREEN)
    }

    private fun setError(error: String) {
        if (dialog.isShowing) {
            dialog.currentFocus?.let {
                Log.d("FOCUS", it.accessibilityClassName.toString())
                Snackbar.make(it, error, Snackbar.LENGTH_SHORT).show()
            }
        } else {
            showStandardScreen()
            Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showProgress() {
        binding.apply {
            loginFieldLayout.visibility = View.GONE
            passwordFieldLayout.visibility = View.GONE
            createAccountButton.visibility = View.GONE
            loginButton.visibility = View.GONE
            forgotPasswordButton.visibility = View.GONE
            loginScreenProgressbar.visibility = View.VISIBLE
        }
    }

    private fun showDialogVerifyProgress() {

        Log.d("TIME", Calendar.getInstance().time.toString())

        if (dialog.isShowing) {

            dialog.currentFocus?.let {
                verifySnackbar = Snackbar.make(
                    it.rootView,
                    "Verifying...",
                    Snackbar.LENGTH_INDEFINITE
                )

                verifySnackbar?.show()
            }
        }
    }

    private fun showStandardScreen() {
        binding.apply {
            loginFieldLayout.visibility = View.VISIBLE
            passwordFieldLayout.visibility = View.VISIBLE
            createAccountButton.visibility = View.VISIBLE
            loginButton.visibility = View.VISIBLE
            forgotPasswordButton.visibility = View.VISIBLE
            loginScreenProgressbar.visibility = View.GONE
        }
    }

    private fun showPasswordChangeDialog() {

        dialog.apply {

            val loginEmail =
                dialog.findViewById<TextInputEditText>(R.id.bottom_sheet_login_field_edit_text)
            val verifyButton = dialog.findViewById<MaterialButton>(R.id.send_verify_email_button)

            verifyButton.setOnClickListener {
                viewModel.verifyEmail(loginEmail.text.toString())
            }

            window?.apply {
                setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setGravity(Gravity.BOTTOM)
            }
            show()
        }
    }

    private fun enableDialogPasswordChangeFields() {
        val loginEmail =
            dialog.findViewById<TextInputEditText>(R.id.bottom_sheet_login_field_edit_text)
        val verifyButton = dialog.findViewById<MaterialButton>(R.id.send_verify_email_button)
        val newPassword =
            dialog.findViewById<TextInputEditText>(R.id.new_password_field_edit_text)
        val changePasswordButton = dialog.findViewById<MaterialButton>(R.id.change_password_button)

        verifyButton.isEnabled = false
        Log.d("TIME", Calendar.getInstance().time.toString())
        verifySnackbar?.dismiss()
        newPassword.isEnabled = true
        changePasswordButton.isEnabled = true

        changePasswordButton.setOnClickListener {
            viewModel.changePassword(loginEmail.text.toString(), newPassword.text.toString())
            hideKeyboard(dialog.currentFocus)
            verifyButton.isEnabled = true
            newPassword.isEnabled = false
            changePasswordButton.isEnabled = false
            loginEmail.text?.clear()
            newPassword.text?.clear()
            dialog.dismiss()
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus

            if (view is TextInputEditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    view.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboard(view: View?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun restoreViewModel(): LoginViewModel {
        val restoredViewModel = lastCustomNonConfigurationInstance as? LoginViewModel
        return restoredViewModel ?: LoginViewModel(app.loginInteractor)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return viewModel
    }
}