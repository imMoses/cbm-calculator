package id.cbm.main.cbm_calculator.ui.engineer.form.customer // ktlint-disable package-name

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import id.cbm.main.cbm_calculator.BuildConfig
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.data.local.LocalDataController
import id.cbm.main.cbm_calculator.data.remote.dto.SignInResponse
import id.cbm.main.cbm_calculator.databinding.ActivityDataCustomerBinding
import id.cbm.main.cbm_calculator.ui.engineer.form.RequestFormActivity
import id.cbm.main.cbm_calculator.utils.LocalData
import id.cbm.main.cbm_calculator.utils.setSafeOnClickListener

class DataCustomerActivity : BaseActivity<ActivityDataCustomerBinding>() {

    companion object {
        const val DC_PERHITUNGAN = "DATACUSTOMER_PERHITUNGAN"
        const val DC_CUSTOMER = "DC_CUSTOMER"
        const val DC_PROYEK = "DC_PROYEK"
        const val DC_SALES = "DC_SALES"
        const val DC_ASAS = "DC_ASAS"
    }

    override fun getViewBinding() = ActivityDataCustomerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (BuildConfig.DEBUG) {
//            binding.apply {
//                etPerhitunganNo.setText("222093")
//                etCustomer.setText("Customer A")
//                etProyek.setText("Project Asgard")
//                etAsas.setText("Test asas")
//            }
//        }
        initUI()
        initListener()
    }

    private fun initUI() {
        binding.apply {
            val userData = LocalDataController.General().getModel(
                context = this@DataCustomerActivity,
                key = LocalData.Key.USER_PERSONAL_DATA,
                objType = SignInResponse::class.java,
                sharedPref = LocalData.DB.ADDITIONAL_DATA_USER,
            )
            etSales.setText(userData?.name ?: "")
        }
    }

    private fun initListener() {
        binding.apply {
            appBarCustomer.setNavBackListener { onCustomBackPressed() }

            btnNext.setSafeOnClickListener {
                if (etPerhitunganNo.text.isNullOrBlank()) {
                    etPerhitunganNo.error = "Mohon dilengkapi"
                    return@setSafeOnClickListener
                }

                if (etCustomer.text.isNullOrBlank()) {
                    etCustomer.error = "Mohon dilengkapi nama Customer"
                    return@setSafeOnClickListener
                }

                if (etProyek.text.isNullOrBlank()) {
                    etProyek.error = "Mohon dilengkapi nama Proyek"
                    return@setSafeOnClickListener
                }

                val intent = Intent(this@DataCustomerActivity, RequestFormActivity::class.java)
                intent.putExtra(DC_PERHITUNGAN, etPerhitunganNo.text.toString().trim())
                intent.putExtra(DC_CUSTOMER, etCustomer.text.toString().trim())
                intent.putExtra(DC_PROYEK, etProyek.text.toString().trim())
                intent.putExtra(DC_SALES, etSales.text.toString().trim())
                intent.putExtra(DC_ASAS, etAsas.text.toString().trim())
                startActivity(intent)

            }
        }
    }
}
