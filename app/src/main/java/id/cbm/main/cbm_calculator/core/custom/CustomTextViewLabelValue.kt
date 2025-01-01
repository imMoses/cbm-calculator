package id.cbm.main.cbm_calculator.core.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.databinding.CustomTextviewLabelValueBinding

class CustomTextViewLabelValue @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = CustomTextviewLabelValueBinding.inflate(
        LayoutInflater.from(context),
        this,
        true,
    )

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextViewLabelValue)

        val labelText = typeArray.getString(R.styleable.CustomTextViewLabelValue_labelText)
        val valueText = typeArray.getString(R.styleable.CustomTextViewLabelValue_valueText)

        binding.tvLabel.text = labelText
        binding.tvValue.text = valueText

        typeArray.recycle()
    }

    fun setLabelText(text: String) {
        binding.tvLabel.text = text
    }

    fun setValueText(valueText: String) {
        binding.tvValue.text = valueText
    }
}