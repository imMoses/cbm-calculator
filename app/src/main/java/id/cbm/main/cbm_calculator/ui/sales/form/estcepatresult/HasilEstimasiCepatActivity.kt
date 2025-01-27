package id.cbm.main.cbm_calculator.ui.sales.form.estcepatresult // ktlint-disable package-name

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.databinding.ActivityHasilEstimasiCepatBinding
import id.cbm.main.cbm_calculator.ui.sales.adapter.ListMaterialAdapter
import id.cbm.main.cbm_calculator.ui.sales.helper.EstimationCepatHelper
import id.cbm.main.cbm_calculator.ui.sales.helper.SpaceItemDecoration
import id.cbm.main.cbm_calculator.ui.sales.model.HasilEstimasiModel
import id.cbm.main.cbm_calculator.utils.setSafeOnClickListener
import id.cbm.main.cbm_calculator.utils.toIDR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class HasilEstimasiCepatActivity : BaseActivity<ActivityHasilEstimasiCepatBinding>() {

    companion object {

        const val HASIL_NAMA_PELANGGAN = "hasil_nama_pelanggan"
        const val HASIL_NAMA_PROYEK = "hasil_nama_proyek"
        fun intentActivity(context: Context, namaPelanggan: String, namaProyek: String): Intent {
            return Intent(context, HasilEstimasiCepatActivity::class.java).apply {
                putExtra(HASIL_NAMA_PELANGGAN, namaPelanggan)
                putExtra(HASIL_NAMA_PROYEK, namaProyek)
            }
        }
    }

    private var namaPelanggan = ""
    private var namaProyek = ""

    private lateinit var adapterListMaterial: ListMaterialAdapter
    private val helper by lazy { EstimationCepatHelper(this) }

    override fun getViewBinding() = ActivityHasilEstimasiCepatBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        namaPelanggan = intent.getStringExtra(HASIL_NAMA_PELANGGAN) ?: ""
        namaProyek = intent.getStringExtra(HASIL_NAMA_PROYEK) ?: ""

        initView()
        initListener()
    }

    private fun initView() {
        binding.apply {
            tvNamaPelanggan.text = namaPelanggan
            tvProyekName.text = "Proyek: $namaProyek"

            adapterListMaterial = ListMaterialAdapter()
            adapterListMaterial.submitList(helper.addListMaterial())

            rvHasilEstimasi.layoutManager = LinearLayoutManager(this@HasilEstimasiCepatActivity)
            rvHasilEstimasi.addItemDecoration(SpaceItemDecoration(30))
            rvHasilEstimasi.adapter = adapterListMaterial

            updateViewTotalEstimasi(adapterListMaterial.currentList)
        }
    }

    private fun initListener() {
        binding.apply {
            appBarHasilEstCepat.setNavBackListener {
                finish()
            }

            adapterListMaterial.onUpdateTotalPrice(object : ListMaterialAdapter.ListMaterialListener {
                override fun onUpdateTotalPrice(totalPrices: MutableList<HasilEstimasiModel>) {
                    updateViewTotalEstimasi(totalPrices)
                }
            })

            btnSimpan.setSafeOnClickListener {
                Toast.makeText(this@HasilEstimasiCepatActivity, "Under maintenance", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateViewTotalEstimasi(totalPrices: MutableList<HasilEstimasiModel>) {
        lifecycleScope.launch {
            val finalTotalPrice = withContext(Dispatchers.IO) {
                calculateTotalPrice(totalPrices)
            }

            // update UI
            binding.tvValueTotalKeseluruhan.text = finalTotalPrice.toIDR(withCurrency = true)
        }
    }

    private fun calculateTotalPrice(list: MutableList<HasilEstimasiModel>) : String {
        var total = 0.0
        list.forEach {
            Log.i(HasilEstimasiCepatActivity::class.simpleName, "total price: ${it.total}")
            total += it.total.toDouble()
        }

        return String.format(Locale.US, "%.2f", total)
    }
}
