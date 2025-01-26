package id.cbm.main.cbm_calculator.ui.sales.form.customer // ktlint-disable package-name

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.get
import id.cbm.main.cbm_calculator.BuildConfig
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.data.local.LocalDataController
import id.cbm.main.cbm_calculator.data.remote.dto.SignInResponse
import id.cbm.main.cbm_calculator.databinding.ActivityDataCustFormSalesBinding
import id.cbm.main.cbm_calculator.ui.sales.form.estcepat.EstCepatActivity
import id.cbm.main.cbm_calculator.utils.LocalData
import id.cbm.main.cbm_calculator.utils.setSafeOnClickListener

class DataCustFormSalesActivity : BaseActivity<ActivityDataCustFormSalesBinding>() {

    override fun getViewBinding() = ActivityDataCustFormSalesBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            binding.apply {
                etNamaProyek.setValueText("PT ASN")
                etAlamat.setValueText("Jakarta Pusat")
                etNamaPelanggan.setValueText("Moses")
                etKodeEstimasi.setValueText("Bandung")
            }
        }

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

        binding.etSales.setValueText(userData?.name ?: "")
    }

    private fun initListener() {
        binding.apply {
            appBarSales.setNavBackListener {
                finish()
            }

            btnNext.setSafeOnClickListener {
                submitDataCustomer()
            }
        }
    }

    private fun submitDataCustomer() {
        binding.apply {
            if (etNamaProyek.getValueText().isEmpty()) {
                showToast("Mohon masukkan nama proyek")
                return
            }

            if (etAlamat.getValueText().isEmpty()) {
                showToast("Mohon input alamat")
                return
            }

            if (etNamaPelanggan.getValueText().isEmpty()) {
                showToast("Mohon input nama pelanggan")
                return
            }

            if (etSales.getValueText().isEmpty()) {
                showToast("Mohon input sales")
                return
            }

            if (etKodeEstimasi.getValueText().isEmpty()) {
                showToast("Mohon input kode estimasi")
                return
            }

            startActivity(
                EstCepatActivity.intentActivty(
                    context = this@DataCustFormSalesActivity,
                    namaProyek = etNamaProyek.getValueText(),
                    alamat = etAlamat.getValueText(),
                    namaPelanggan = etNamaPelanggan.getValueText(),
                    sales = etSales.getValueText(),
                    kodeEstimasi = etKodeEstimasi.getValueText(),
                ),
            )
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
