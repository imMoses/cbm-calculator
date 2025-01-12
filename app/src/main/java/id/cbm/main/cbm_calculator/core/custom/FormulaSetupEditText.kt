package id.cbm.main.cbm_calculator.core.custom

import android.content.Context
import android.text.SpannableString
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.databinding.FormulaSetupEdittextBinding

class FormulaSetupEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = FormulaSetupEdittextBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.FormulaSetupEditText)

        val valueTextAligment = typeArray.getInt(R.styleable.FormulaSetupEditText_alignmentText, 0)

        binding.tvLeftText.text = typeArray.getString(R.styleable.FormulaSetupEditText_leftTextMainFormula)
        binding.tvRow1.text = typeArray.getString(R.styleable.FormulaSetupEditText_upperFormulaText)
        binding.tvRow2.text = typeArray.getString(R.styleable.FormulaSetupEditText_bottomFormulaText)
        binding.tvUnit.text = typeArray.getString(R.styleable.FormulaSetupEditText_unitFormula)
        binding.etValueText.isEnabled = !typeArray.getBoolean(R.styleable.FormulaSetupEditText_disabledInputEditText, false)

        when (valueTextAligment) {
            1 -> {
                binding.etValueText.textAlignment = TEXT_ALIGNMENT_TEXT_END
            }
            else -> {
                binding.etValueText.textAlignment = TEXT_ALIGNMENT_TEXT_START
            }
        }

        typeArray.recycle()
    }

    fun setLeftText(text: String) {
        binding.tvLeftText.text = text
    }

    fun setUpperFormulaText(spanText: SpannableString) {
        binding.tvRow1.text = spanText
    }

    fun setBottomFormulaText(spanText: SpannableString) {
        binding.tvRow2.text = spanText
    }

    fun setUnitResult(text: SpannableString) {
        binding.tvUnit.text = text
    }

    fun getEditTextValue() : EditText {
        return binding.etValueText
    }
}