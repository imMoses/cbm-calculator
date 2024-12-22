package id.cbm.main.cbm_calculator.core.custom

import android.content.Context
import android.text.SpannableString
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.databinding.CustomEdittextFormulaUnitBinding

class CustomEditTextCalculation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = CustomEdittextFormulaUnitBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditTextCalculation)

        val titleSectionText = typeArray.getString(R.styleable.CustomEditTextCalculation_titleSection)
        val leftSymbolText = typeArray.getString(R.styleable.CustomEditTextCalculation_leftSymbol)
        val unitText = typeArray.getString(R.styleable.CustomEditTextCalculation_unit)

        binding.tvTitleColumn.text = titleSectionText
        binding.tvSymbol.text = leftSymbolText
        binding.tvUnit.text = unitText

        typeArray.recycle()
    }

    fun setTextCustomSpannebleSymbol(span: SpannableString) {
        binding.tvSymbol.text = span
    }

    fun isEnableEditText(isEnable: Boolean) {
        binding.etNumberInput.isEnabled = isEnable
    }

    fun getText(): String {
        return binding.etNumberInput.text.toString().trim()
    }
}