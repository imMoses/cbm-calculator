package id.cbm.main.cbm_calculator.ui.mainengineer // ktlint-disable package-name

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.databinding.ActivityMainEngineerBinding

class MainEngineerActivity : BaseActivity<ActivityMainEngineerBinding>() {

    override fun getViewBinding() = ActivityMainEngineerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}
