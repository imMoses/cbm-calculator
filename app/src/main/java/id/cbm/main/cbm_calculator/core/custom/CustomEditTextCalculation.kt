package id.cbm.main.cbm_calculator.core.custom

import android.content.Context
import android.text.SpannableString
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
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
        val valueTextAligment = typeArray.getInt(R.styleable.CustomEditTextCalculation_valueTextAligment, 0)
        val disableInputText = typeArray.getBoolean(R.styleable.CustomEditTextCalculation_disabledInputText, false)
        val addFormulaInfo = typeArray.getBoolean(R.styleable.CustomEditTextCalculation_addFormulaInfo, false)

        binding.tvTitleColumn.text = titleSectionText
        binding.tvSymbol.text = leftSymbolText
        binding.tvUnit.text = unitText
        binding.etNumberInput.isEnabled = !disableInputText

        when (valueTextAligment) {
            1 -> {
                binding.etNumberInput.textAlignment = TEXT_ALIGNMENT_TEXT_END
            }
            else -> {
                binding.etNumberInput.textAlignment = TEXT_ALIGNMENT_TEXT_START
            }
        }

        binding.tvFormulaInfo.visibility = if (addFormulaInfo) {
            View.VISIBLE
        } else {
            View.GONE
        }

        typeArray.recycle()
    }

    fun setTitleText(span: SpannableString) {
        binding.tvTitleColumn.text = span
    }

    fun setSymbolTextSpanneble(span: SpannableString) {
        binding.tvSymbol.text = span
    }

    fun setUnitTextSpannable(span: SpannableString) {
        binding.tvUnit.text = span
    }

    fun setFormulaTextInfoSpannable(span: SpannableString) {
        binding.tvFormulaInfo.text = span
    }

    fun addTextChangeListener(listener: TextWatcher) {
        binding.etNumberInput.addTextChangedListener(listener)
    }

    fun setSelection(index: Int) {
        binding.etNumberInput.setSelection(index)
    }

    fun setValueText(text: String) {
        binding.etNumberInput.setText(text)
    }

    fun isEnableEditText(isEnable: Boolean) {
        binding.etNumberInput.isEnabled = isEnable
    }

    fun getText(): String {
        return binding.etNumberInput.text.toString().trim()
    }
}