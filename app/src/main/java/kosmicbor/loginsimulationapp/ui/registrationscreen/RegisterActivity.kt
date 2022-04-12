package kosmicbor.loginsimulationapp.ui.registrationscreen

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kosmicbor.loginsimulationapp.app
import kosmicbor.loginsimulationapp.ui.loginscreen.LoginActivity
import kosmicbor.loginsimulationapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegistrationContract.RegistrationViewModel
    private val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel = RegistrationViewModel(app.registrationInteractor)

        viewModel.isInProgress.subscribe(handler) {
            showProgress()
        }

        viewModel.isRegistrationSuccess.subscribe(handler) {
            showStandardScreen()
            setSuccess()
        }

        viewModel.isError.subscribe(handler) {
            it?.let { it1 -> setError(it1) }
        }

        binding.registerButton.setOnClickListener {


            viewModel.apply {

                onRegistration(
                    binding.newNameFieldEditText.text.toString(),
                    binding.newLoginFieldEditText.text.toString(),
                    binding.newPasswordFieldEditText.text.toString(),
                    binding.newPasswordRepeatFieldEditText.text.toString()
                )
            }
        }
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

    private fun setSuccess() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun setError(error: String) {
        showStandardScreen()
        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
    }

    private fun showProgress() {
        binding.apply {
            binding.newNameFieldEditText.visibility = View.GONE
            binding.newLoginFieldEditText.visibility = View.GONE
            binding.newPasswordFieldEditText.visibility = View.GONE
            binding.newPasswordRepeatFieldEditText.visibility = View.GONE
            binding.registerButton.visibility = View.GONE
            registerScreenProgressbar.visibility = View.VISIBLE
        }
    }

    private fun showStandardScreen() {
        binding.apply {
            binding.newNameFieldEditText.visibility = View.VISIBLE
            binding.newLoginFieldEditText.visibility = View.VISIBLE
            binding.newPasswordFieldEditText.visibility = View.VISIBLE
            binding.newPasswordRepeatFieldEditText.visibility = View.VISIBLE
            binding.registerButton.visibility = View.VISIBLE
            registerScreenProgressbar.visibility = View.GONE
        }
    }
}