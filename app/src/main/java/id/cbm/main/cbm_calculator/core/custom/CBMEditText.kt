package id.cbm.main.cbm_calculator.core.custom // ktlint-disable package-name

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.databinding.CbmEdittextBinding

class CBMEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = CbmEdittextBinding.inflate(LayoutInflater.from(context), this, true)
    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CBMEditText)

        val labelName = typeArray.getString(R.styleable.CBMEditText_labelName)
        val isInfoIconAppear = typeArray.getBoolean(R.styleable.CBMEditText_isInfoNeeded, false)
        val inputType = typeArray.getInt(R.styleable.CBMEditText_inputTypeEditText, TYPE_INPUT_TEXT)

        binding.apply {
            tvLabelName.text = labelName

            ivInfo.visibility = if (isInfoIconAppear) {
                View.VISIBLE
            } else {
                View.GONE
            }

            when (inputType) {
                TYPE_INPUT_NUMBER -> etValueText.inputType = InputType.TYPE_CLASS_NUMBER
                TYPE_INPUT_NUMBER_DECIMAL -> etValueText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                else -> etValueText.inputType = InputType.TYPE_CLASS_TEXT
            }
        }

        typeArray.recycle()
    }

    fun setLabelName(labelName: String) {
        binding.tvLabelName.text = labelName
    }

    fun getValueText(): String {
        return binding.etValueText.text.toString().trim()
    }

    fun setValueText(text: String) {
        binding.etValueText.setText(text)
    }

    fun getEditText(): EditText {
        return binding.etValueText
    }

    fun setInfoClickListener(listener: View.OnClickListener) {
        binding.ivInfo.setOnClickListener(listener)
    }

    companion object {
        const val TYPE_INPUT_TEXT = 101
        const val TYPE_INPUT_NUMBER = 102
        const val TYPE_INPUT_NUMBER_DECIMAL = 103
    }
}
