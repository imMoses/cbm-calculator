package id.cbm.main.cbm_calculator.ui.signin // ktlint-disable package-name

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import id.cbm.main.cbm_calculator.BuildConfig
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.databinding.ActivitySignInBinding
import id.cbm.main.cbm_calculator.ui.engineer.form.RequestFormActivity
import id.cbm.main.cbm_calculator.ui.mainengineer.MainEngineerActivity
import id.cbm.main.cbm_calculator.utils.setSafeOnClickListener

class SignInActivity : BaseActivity<ActivitySignInBinding>() {

    override fun getViewBinding() = ActivitySignInBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            binding.tieEmail.setText("hello@gmail.com")
            binding.tiePassword.setText("test123")
        }
        binding.tvAppVersion.text = String.format("v.%s", BuildConfig.VERSION_NAME)

        binding.btnSignIn.setSafeOnClickListener {
            validateSignIn()
        }
    }

    fun validateSignIn() {
        binding.apply {
            if (tieEmail.text.isNullOrBlank()) {
                tieEmail.error = "Please input your email"
                return
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(tieEmail.text.toString().trim()).matches()) {
                tieEmail.error = "Email format invalid"
                return
            }

            if (tiePassword.text.isNullOrBlank()) {
                tiePassword.error = "Please input password sign in"
                return
            }

            Toast.makeText(this@SignInActivity, "Success", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this@SignInActivity, MainEngineerActivity::class.java)
            )
            finishAffinity()
        }
    }
}
