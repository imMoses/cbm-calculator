package id.cbm.main.cbm_calculator.ui.splashscreen // ktlint-disable package-name

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import id.cbm.main.cbm_calculator.databinding.ActivitySplashScreenBinding
import id.cbm.main.cbm_calculator.ui.signin.SignInActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var _binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        startActivity(
            Intent(
                this,
                SignInActivity::class.java,
            )
        )
        finishAffinity()
    }
}
