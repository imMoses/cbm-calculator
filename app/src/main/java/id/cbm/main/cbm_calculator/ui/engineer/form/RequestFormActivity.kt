package id.cbm.main.cbm_calculator.ui.engineer.form

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.widget.Toast
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.databinding.ActivityRequestFormBinding
import id.cbm.main.cbm_calculator.ui.engineer.form.customer.DataCustomerActivity
import id.cbm.main.cbm_calculator.utils.CustomRegex
import id.cbm.main.cbm_calculator.utils.setSafeOnClickListener

class RequestFormActivity : BaseActivity<ActivityRequestFormBinding>() {

    private var bentangPlatY = 0.0
    private var bentangPlayX = 0.0

    private var dataPerhitunganNo = "-"
    private var dataCust = "-"
    private var dataProyek = "-"
    private var dataSales = "-"
    private var dataAsas = "-"

    override fun getViewBinding() = ActivityRequestFormBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initIntentExtras(savedInstanceState)
        initUI()
        initListener()
        resetKoefisienResult()
    }

    private fun initIntentExtras(savedInstanceState: Bundle?) {
        intent?.let {
            dataPerhitunganNo = intent.getStringExtra(DataCustomerActivity.DC_PERHITUNGAN) ?: "-"
            dataCust = intent.getStringExtra(DataCustomerActivity.DC_CUSTOMER) ?: "-"
            dataProyek = intent.getStringExtra(DataCustomerActivity.DC_PROYEK) ?: "-"
            dataSales = intent.getStringExtra(DataCustomerActivity.DC_SALES) ?: "-"
            dataAsas = intent.getStringExtra(DataCustomerActivity.DC_ASAS) ?: "-"

            binding.apply {
                ctvNoPerhitungan.setValueText(dataPerhitunganNo)
                ctvCustomer.setValueText(dataCust)
                ctvProyek.setValueText(dataProyek)
                ctvSales.setValueText(dataSales)
                ctvAsAs.setValueText(dataAsas)
            }
        }
    }

    private fun initUI() {
        binding.apply {
            appBar.setNavBackListener { onCustomBackPressed() }

            etKuatTarikBeton.setValueText("2,17")

            val textFy = SpannableString("fy")
            textFy.setSpan(SubscriptSpan(), 1, 2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            textFy.setSpan(RelativeSizeSpan(0.8f), 1,2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            etMetalYield.setSymbolTextSpanneble(textFy)
            etPanjangBentangPlatX.setSymbolTextSpanneble(textFy)

            val textFc1 = SpannableString(resources.getString(R.string.symbol_fc1))
            textFc1.setSpan(SubscriptSpan(), 1, 2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            textFc1.setSpan(RelativeSizeSpan(0.8f), 1,2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            etKuatTekanBeton.setSymbolTextSpanneble(textFc1)

            val textFct = SpannableString("fct")
            textFct.setSpan(SubscriptSpan(), 1, 3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            textFct.setSpan(RelativeSizeSpan(0.8f), 1,3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            etKuatTarikBeton.setSymbolTextSpanneble(textFc1)

            val textFst = SpannableString("fst")
            textFst.setSpan(SubscriptSpan(), 1, 3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            textFst.setSpan(RelativeSizeSpan(0.8f), 1,3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            etTeganganLeleh.setSymbolTextSpanneble(textFst)

            val textLy = SpannableString("Ly")
            textLy.setSpan(SubscriptSpan(), 1, 2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            textLy.setSpan(RelativeSizeSpan(0.8f), 1,2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            etPanjangBentangPlatY.setSymbolTextSpanneble(textLy)
        }
    }

    private fun initListener() {
        binding.apply {
            etPanjangBentangPlatY.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    text?.let {
                        if (it.length > 1) {
                            val regex = CustomRegex.ZERO_FIRST_NOT_ALLOWED.toRegex()
                            if (it.contains(regex)) {
                                val cleanText = it.replace(regex, "")
                                etPanjangBentangPlatY.setValueText(cleanText)
                                etPanjangBentangPlatY.setSelection(cleanText.length)
                            }
                        }
                    }
                }

                override fun afterTextChanged(textInput: Editable?) {
                    val text = textInput.toString()
                    if (text.isNotEmpty()) {
                        bentangPlatY = text.toDoubleOrNull() ?: 0.0
                        calculateKoefisien()
                    } else {
                        resetKoefisienResult()
                        bentangPlatY = 0.0
                    }
                }
            })

            etPanjangBentangPlatX.addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    text?.let {
                        if (it.length > 1) {
                            val regex = CustomRegex.ZERO_FIRST_NOT_ALLOWED.toRegex()
                            if (it.contains(regex)) {
                                val cleanText = it.replace(regex, "")
                                etPanjangBentangPlatX.setValueText(cleanText)
                                etPanjangBentangPlatX.setSelection(cleanText.length)
                            }
                        }
                    }
                }

                override fun afterTextChanged(textInputted: Editable?) {
                   val text = textInputted.toString()
                    if (text.isNotEmpty()) {
                        bentangPlayX = text.toDoubleOrNull() ?: 0.0
                        calculateKoefisien()
                    } else {
                        resetKoefisienResult()
                        bentangPlayX = 0.0
                    }
                }
            })

            btnSave.setSafeOnClickListener {
                Toast.makeText(this@RequestFormActivity, "Tersimpan!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateKoefisien() {
        if (bentangPlatY > 0.0 && bentangPlayX > 0.0) {
            val result = bentangPlatY / bentangPlayX

            setTextKoefisien(result)

            val koefisienThreshold = 2.0
            if (result >= koefisienThreshold) {
                binding.tvResultPossibility.text =
                    resources.getString(R.string.form_engineer_koefisien_pelat_calc_correct)
            } else {
                binding.tvResultPossibility.text =
                    resources.getString(R.string.form_engineer_koefisien_pelat_calc_incorrect)
            }
        }
    }

    private fun setTextKoefisien(resCalculation: Double?) {
        val textLetter = if (resCalculation == null) {
            String.format(
                "%s / %s = -",
                resources.getString(R.string.symbol_Ly),
               "Lx",
            )
        } else {
            String.format(
                "%s / %s = %.1f",
                resources.getString(R.string.symbol_Ly),
                "Lx",
                resCalculation,
            )
        }

        val textKoefisien = SpannableString(textLetter)
        // Ly
        textKoefisien.setSpan(SubscriptSpan(), 1, 2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        textKoefisien.setSpan(RelativeSizeSpan(0.8f), 1,2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Lx
        textKoefisien.setSpan(SubscriptSpan(), 6, 7, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        textKoefisien.setSpan(RelativeSizeSpan(0.8f), 6,7, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvResultCalculation.text = textKoefisien
    }

    private fun resetKoefisienResult() {
        setTextKoefisien(null)
        binding.tvResultPossibility.text = "-"
    }
}
