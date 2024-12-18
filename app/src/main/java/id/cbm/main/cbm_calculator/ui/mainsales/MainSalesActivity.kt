package id.cbm.main.cbm_calculator.ui.mainsales // ktlint-disable package-name

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.databinding.ActivityMainSalesBinding

class MainSalesActivity : BaseActivity<ActivityMainSalesBinding>() {

    override fun getViewBinding() = ActivityMainSalesBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
