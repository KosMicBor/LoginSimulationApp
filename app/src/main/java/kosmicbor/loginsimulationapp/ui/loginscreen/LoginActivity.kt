package kosmicbor.loginsimulationapp.ui.loginscreen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
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

class LoginActivity : AppCompatActivity(), LoginContract.LoginView {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var presenter: LoginPresenter
    private lateinit var dialog: Dialog
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = Dialog(this)

        snackbar = dialog.currentFocus?.let { Snackbar.make(it, "Verifying...", Snackbar.LENGTH_INDEFINITE) }

        presenter = restorePresenter()

        presenter.onAttach(this@LoginActivity)

        binding.loginButton.setOnClickListener {
            presenter.onLogIn(
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

    override fun setSuccess() {
        binding.loginScreenProgressbar.visibility = View.GONE
        binding.root.setBackgroundColor(Color.GREEN)
    }

    override fun setError(error: String) {
        if (dialog.isShowing) {
            dialog.currentFocus?.let { Snackbar.make(it, error, Snackbar.LENGTH_SHORT).show() }
        } else {
            showStandardScreen()
            Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun showProgress() {
        binding.apply {
            loginFieldLayout.visibility = View.GONE
            passwordFieldLayout.visibility = View.GONE
            createAccountButton.visibility = View.GONE
            loginButton.visibility = View.GONE
            forgotPasswordButton.visibility = View.GONE
            loginScreenProgressbar.visibility = View.VISIBLE
        }
    }

    override fun showStandardScreen() {
        binding.apply {
            loginFieldLayout.visibility = View.VISIBLE
            passwordFieldLayout.visibility = View.VISIBLE
            createAccountButton.visibility = View.VISIBLE
            loginButton.visibility = View.VISIBLE
            forgotPasswordButton.visibility = View.VISIBLE
            loginScreenProgressbar.visibility = View.GONE
        }
    }

    override fun showPasswordChangeDialog() {

        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.bottomsheet_password_change)

            val loginEmail =
                dialog.findViewById<TextInputEditText>(R.id.bottom_sheet_login_field_edit_text)
            val verifyButton = dialog.findViewById<MaterialButton>(R.id.send_verify_email_button)

            verifyButton.setOnClickListener {
                presenter.onVerifyEmail(loginEmail.text.toString())
                snackbar?.show()
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

    override fun enableDialogPasswordChangeFields() {
        val loginEmail =
            dialog.findViewById<TextInputEditText>(R.id.bottom_sheet_login_field_edit_text)
        val verifyButton = dialog.findViewById<MaterialButton>(R.id.send_verify_email_button)
        val newPassword =
            dialog.findViewById<TextInputEditText>(R.id.new_password_field_edit_text)
        val changePasswordButton = dialog.findViewById<MaterialButton>(R.id.change_password_button)

        verifyButton.isEnabled = false
        snackbar?.dismiss()
        newPassword.isEnabled = true
        changePasswordButton.isEnabled = true

        changePasswordButton.setOnClickListener {
            hideKeyboard(dialog.currentFocus)
            dialog.dismiss()
            presenter.onPasswordChanged(loginEmail.text.toString(), newPassword.text.toString())
        }
    }

    override fun showMessage(message: String) {
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

    private fun restorePresenter(): LoginPresenter {
        val restoredPresenter = lastCustomNonConfigurationInstance as? LoginPresenter
        return restoredPresenter ?: LoginPresenter(app.loginInteractor)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return presenter
    }
}