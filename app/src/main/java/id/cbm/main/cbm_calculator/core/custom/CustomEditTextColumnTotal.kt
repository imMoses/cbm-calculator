package id.cbm.main.cbm_calculator.core.custom

import android.content.Context
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.databinding.CustomEdittextColumnTotalBinding
import id.cbm.main.cbm_calculator.databinding.CustomEdittextThreeColumnsBinding
import java.lang.Exception

class CustomEditTextColumnTotal@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = CustomEdittextColumnTotalBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val label = context.getString(R.string.form_engineer_row_total_beban_mati)
        val addQD = label + " QD"
        val textLabel = SpannableString(addQD)
        textLabel.setSpan(SubscriptSpan(), label.length + 2, addQD.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        textLabel.setSpan(RelativeSizeSpan(0.7f), label.length + 2, addQD.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvTotalBebanMatiQ.text = textLabel
    }

    fun getEtTotalQ(): EditText {
        return binding.etTotalQ
    }

    fun getEtFinalTotalQ(): EditText {
        return binding.etFinalTotalQ
    }
}
