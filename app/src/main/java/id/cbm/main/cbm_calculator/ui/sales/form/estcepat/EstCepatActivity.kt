package id.cbm.main.cbm_calculator.ui.sales.form.estcepat // ktlint-disable package-name

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.core.custom.CBMSpinnerText
import id.cbm.main.cbm_calculator.core.custom.modaldialog.DialogList
import id.cbm.main.cbm_calculator.core.custom.modaldialog.data.GeneralModel
import id.cbm.main.cbm_calculator.core.custom.modaldialog.interfaces.DialogListClickListener
import id.cbm.main.cbm_calculator.databinding.ActivityEstimasiCepatBinding
import id.cbm.main.cbm_calculator.ui.sales.adapter.AreaAdapter
import id.cbm.main.cbm_calculator.ui.sales.adapter.ParentAtapAnakAdapter
import id.cbm.main.cbm_calculator.ui.sales.form.estcepatresult.HasilEstimasiCepatActivity
import id.cbm.main.cbm_calculator.ui.sales.helper.EstimationCepatHelper
import id.cbm.main.cbm_calculator.ui.sales.model.AreaModel
import id.cbm.main.cbm_calculator.utils.setSafeOnClickListener

class EstCepatActivity : BaseActivity<ActivityEstimasiCepatBinding>() {

    companion object {

        const val NAMA_PROYEK = "nama_proyek"
        const val ALAMAT = "alamat"
        const val NAMA_PELANGGAN = "nama_pelanggan"
        const val SALES = "sales"
        const val KODE_ESTIMASI = "kode_estimasi"
        fun intentActivty(context: Context, namaProyek: String, alamat: String, namaPelanggan: String, sales: String, kodeEstimasi: String): Intent {
            return Intent(context, EstCepatActivity::class.java).apply {
                putExtra(NAMA_PROYEK, namaProyek)
                putExtra(ALAMAT, alamat)
                putExtra(NAMA_PELANGGAN, namaPelanggan)
                putExtra(SALES, sales)
                putExtra(KODE_ESTIMASI, kodeEstimasi)
            }
        }
    }

    private lateinit var adapterArea: AreaAdapter
    private lateinit var adapterParentAtapAnak: ParentAtapAnakAdapter
    private lateinit var estCepatHelper: EstimationCepatHelper
    override fun getViewBinding() = ActivityEstimasiCepatBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        binding.apply {
            estCepatHelper = EstimationCepatHelper(this@EstCepatActivity)

            adapterArea = AreaAdapter(estCepatHelper.addAreaItems())
            rvArea.layoutManager = GridLayoutManager(this@EstCepatActivity, 2)
            rvArea.adapter = adapterArea

            adapterParentAtapAnak = ParentAtapAnakAdapter(supportFragmentManager)
            rvAnakTangga.layoutManager = LinearLayoutManager(this@EstCepatActivity)
            rvAnakTangga.adapter = adapterParentAtapAnak
        }
    }

    private fun initListener() {
        binding.apply {
            appBarEstCepat.setNavBackListener {
                finish()
            }

            adapterArea.setOnClickItemSpinnerClick(object : AreaAdapter.AreaAdapterSpinnerListener {
                override fun onClickSpinner(data: AreaModel, cbmSpinnerText: CBMSpinnerText) {
                    when (data.labelName) {
                        resources.getString(R.string.form_est_cepat_jenis_penutup) -> {
                            DialogList.Builder()
                                .title(resources.getString(R.string.form_est_cepat_jenis_penutup))
                                .data(data.listItemSpinner ?: mutableListOf())
                                .listener(object : DialogListClickListener {
                                    override fun itemClick(value: GeneralModel) {
                                        // get jenis penutup selected
                                        Toast.makeText(this@EstCepatActivity, value.itemName, Toast.LENGTH_SHORT).show()
                                        // update view selected item
                                        cbmSpinnerText.setTextSpinner(value.itemName ?: "")
                                    }
                                })
                                .overrideDismissListener { it.dismiss() }
                                .build().show(supportFragmentManager, "jenis_penutup")
                        }
                        resources.getString(R.string.form_est_cepat_type_atap) -> {
                            DialogList.Builder()
                                .title(resources.getString(R.string.form_est_cepat_type_atap))
                                .data(data.listItemSpinner ?: mutableListOf())
                                .listener(object : DialogListClickListener {
                                    override fun itemClick(value: GeneralModel) {
                                        // get type atap selected
                                        Toast.makeText(this@EstCepatActivity, value.itemName, Toast.LENGTH_SHORT).show()
                                        // update view selected item
                                        cbmSpinnerText.setTextSpinner(value.itemName ?: "")
                                    }
                                })
                                .overrideDismissListener { it.dismiss() }
                                .build().show(supportFragmentManager, "type_atap")
                        }
                    }
                }
            })

            btnAddAtapAnak.setSafeOnClickListener {
                val indexParentItem = adapterParentAtapAnak.checkTotalList()

                if (indexParentItem < 2) {
                    adapterParentAtapAnak.addItem(estCepatHelper.addAnakAtap(indexParentItem))
                } else {
                    Toast.makeText(this@EstCepatActivity, "Max.2 Atap Anak", Toast.LENGTH_SHORT).show()
                }
            }

            btnSubmit.setSafeOnClickListener {
                startActivity(
                    HasilEstimasiCepatActivity.intentActivity(
                        context = this@EstCepatActivity,
                        namaPelanggan = intent.getStringExtra(NAMA_PELANGGAN) ?: "",
                        namaProyek = intent.getStringExtra(NAMA_PROYEK) ?: "",
                    ),
                )
            }
        }
    }
}
