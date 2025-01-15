package id.cbm.main.cbm_calculator.ui.sales.form.customer // ktlint-disable package-name

import android.os.Bundle
import android.widget.Toast
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.data.local.LocalDataController
import id.cbm.main.cbm_calculator.data.remote.dto.SignInResponse
import id.cbm.main.cbm_calculator.databinding.ActivityDataCustFormSalesBinding
import id.cbm.main.cbm_calculator.utils.LocalData
import id.cbm.main.cbm_calculator.utils.setSafeOnClickListener

class DataCustFormSalesActivity : BaseActivity<ActivityDataCustFormSalesBinding>() {

    override fun getViewBinding() = ActivityDataCustFormSalesBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        initListener()
    }

    private fun initUI() {
        val userData = LocalDataController.General().getModel(
            context = this,
            key = LocalData.Key.USER_PERSONAL_DATA,
            objType = SignInResponse::class.java,
            sharedPref = LocalData.DB.ADDITIONAL_DATA_USER,
        )
        binding.etSales.setText(userData?.name ?: "")
    }

    private fun initListener() {
        binding.apply {
            appBarSales.setNavBackListener {
                finish()
            }

            btnNext.setSafeOnClickListener {
                Toast.makeText(
                    this@DataCustFormSalesActivity,
                    "Coming Soon ...",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}
