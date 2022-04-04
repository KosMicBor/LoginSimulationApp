package kosmicbor.loginsimulationapp.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kosmicbor.loginsimulationapp.LoginActivity
import kosmicbor.loginsimulationapp.LoginContract
import kosmicbor.loginsimulationapp.RegistrationContract
import kosmicbor.loginsimulationapp.databinding.ActivityRegisterBinding
import kosmicbor.loginsimulationapp.presenters.LoginPresenter
import kosmicbor.loginsimulationapp.presenters.RegistrationPresenter

class RegisterActivity : AppCompatActivity(), RegistrationContract.RegistrationView {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var presenter: RegistrationContract.RegistrationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        presenter = RegistrationPresenter().apply {
            onAttach(this@RegisterActivity)
        }

        binding.registerBtn.setOnClickListener {


            presenter.apply {

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

    override fun setSuccess() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun setError(error: String) {
        showStandardScreen()
        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        binding.apply {
            binding.newNameFieldEditText.visibility = View.GONE
            binding.newLoginFieldEditText.visibility = View.GONE
            binding.newPasswordFieldEditText.visibility = View.GONE
            binding.newPasswordRepeatFieldEditText.visibility = View.GONE
            binding.registerBtn.visibility = View.GONE
            registerScreenProgressbar.visibility = View.VISIBLE
        }
    }

    override fun showStandardScreen() {
        binding.apply {
            binding.newNameFieldEditText.visibility = View.VISIBLE
            binding.newLoginFieldEditText.visibility = View.VISIBLE
            binding.newPasswordFieldEditText.visibility = View.VISIBLE
            binding.newPasswordRepeatFieldEditText.visibility = View.VISIBLE
            binding.registerBtn.visibility = View.VISIBLE
            registerScreenProgressbar.visibility = View.GONE
        }
    }
}