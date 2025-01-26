package id.cbm.main.cbm_calculator.ui.sales.form.estcepat // ktlint-disable package-name

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.databinding.ActivityEstimasiCepatBinding
import id.cbm.main.cbm_calculator.ui.sales.adapter.AreaAdapter
import id.cbm.main.cbm_calculator.ui.sales.adapter.ParentAtapAnakAdapter
import id.cbm.main.cbm_calculator.ui.sales.form.estcepatresult.HasilEstimasiCepatActivity
import id.cbm.main.cbm_calculator.ui.sales.helper.EstimationCepatHelper
import id.cbm.main.cbm_calculator.utils.setSafeOnClickListener

class EstCepatActivity : BaseActivity<ActivityEstimasiCepatBinding>() {

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

            adapterParentAtapAnak = ParentAtapAnakAdapter()
            rvAnakTangga.layoutManager = LinearLayoutManager(this@EstCepatActivity)
            rvAnakTangga.adapter = adapterParentAtapAnak
        }
    }

    private fun initListener() {
        binding.apply {
            appBarEstCepat.setNavBackListener {
                finish()
            }

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
                    Intent(
                        this@EstCepatActivity,
                        HasilEstimasiCepatActivity::class.java,
                    ),
                )
            }
        }
    }
}
