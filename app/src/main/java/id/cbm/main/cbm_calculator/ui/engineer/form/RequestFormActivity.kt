package id.cbm.main.cbm_calculator.ui.engineer.form

import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.view.View
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.core.base_ui.BaseActivity
import id.cbm.main.cbm_calculator.databinding.ActivityRequestFormBinding
import id.cbm.main.cbm_calculator.utils.CustomRegex

class RequestFormActivity : BaseActivity<ActivityRequestFormBinding>() {

    private var bentangPlatY = 0.0
    private var bentangPlayX = 0.0

    override fun getViewBinding() = ActivityRequestFormBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        initListener()
        resetKoefisienResult()
    }

    private fun initUI() {
        binding.appBar.setNavBackListener { onCustomBackPressed() }

        binding.etKuatTarikBeton.setText("2,17")

//        val testFqtext = SpannableString("fq")
//        testFqtext.setSpan(SubscriptSpan(), 1, 2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
//        testFqtext.setSpan(RelativeSizeSpan(0.8f), 1,2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
//        binding.etTest.setTextCustomSpannebleSymbol(testFqtext)

        val textFy = SpannableString("fy")
        textFy.setSpan(SubscriptSpan(), 1, 2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        textFy.setSpan(RelativeSizeSpan(0.8f), 1,2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvSymbol1.text = textFy

        val textFc1 = SpannableString(resources.getString(R.string.symbol_fc1))
        textFc1.setSpan(SubscriptSpan(), 1, 2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        textFc1.setSpan(RelativeSizeSpan(0.8f), 1,2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvSymbol6.text = textFc1

        val textFct = SpannableString("fct")
        textFct.setSpan(SubscriptSpan(), 1, 3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        textFct.setSpan(RelativeSizeSpan(0.8f), 1,3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvSymbol7.text = textFct

        val textFst = SpannableString("fst")
        textFst.setSpan(SubscriptSpan(), 1, 3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        textFst.setSpan(RelativeSizeSpan(0.8f), 1,3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvSymbol8.text = textFst

        val textLy = SpannableString("Ly")
        textLy.setSpan(SubscriptSpan(), 1, 2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        textLy.setSpan(RelativeSizeSpan(0.8f), 1,2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvSymbol9.text = textLy

    }

    private fun initListener() {
        binding.apply {
            etPanjangBentangPlatY.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    text?.let {
                        if (it.length > 1) {
                            val regex = CustomRegex.ZERO_FIRST_NOT_ALLOWED.toRegex()
                            if (it.contains(regex)) {
                                val cleanText = it.replace(regex, "")
                                etPanjangBentangPlatY.setText(cleanText)
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

            etPanjangBentangPlatX.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    text?.let {
                        if (it.length > 1) {
                            val regex = CustomRegex.ZERO_FIRST_NOT_ALLOWED.toRegex()
                            if (it.contains(regex)) {
                                val cleanText = it.replace(regex, "")
                                etPanjangBentangPlatX.setText(cleanText)
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
