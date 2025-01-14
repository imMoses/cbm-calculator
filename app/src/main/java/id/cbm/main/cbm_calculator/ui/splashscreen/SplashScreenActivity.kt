package id.cbm.main.cbm_calculator.ui.splashscreen // ktlint-disable package-name

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import id.cbm.main.cbm_calculator.data.local.LocalDataController
import id.cbm.main.cbm_calculator.data.remote.dto.SignInResponse
import id.cbm.main.cbm_calculator.databinding.ActivitySplashScreenBinding
import id.cbm.main.cbm_calculator.ui.mainengineer.MainEngineerActivity
import id.cbm.main.cbm_calculator.ui.signin.SignInActivity
import id.cbm.main.cbm_calculator.utils.LocalData

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var _binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val isLoggedIn = LocalDataController.General().getModel(
            context = this,
            key = LocalData.Key.USER_PERSONAL_DATA,
            objType = SignInResponse::class.java,
            sharedPref = LocalData.DB.ADDITIONAL_DATA_USER,
        )

        if (isLoggedIn != null) {
            if (isLoggedIn.token.isNullOrEmpty()) {
                goToSignInPage()
            } else {
                goToLandingPage()
            }
        } else {
            goToSignInPage()
        }

        finishAffinity()
    }

    private fun goToSignInPage() {
        startActivity(
            Intent(
                this,
                SignInActivity::class.java,
            ),
        )
    }

    private fun goToLandingPage() {
        startActivity(
            Intent(
                this,
                MainEngineerActivity::class.java,
            ),
        )
    }
}
