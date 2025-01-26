package id.cbm.main.cbm_calculator.core.custom // ktlint-disable package-name

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.databinding.CbmSpinnertextBinding

class CBMSpinnerText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = CbmSpinnertextBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CBMSpinnerText)

        val labelName = typeArray.getString(R.styleable.CBMSpinnerText_labelNameSpinner)
        val isInfoNeeded = typeArray.getBoolean(R.styleable.CBMSpinnerText_isInfoSpinnerNeeded, false)

        binding.apply {
            tvLabelName.text = labelName

            ivInfo.visibility = if (isInfoNeeded) View.VISIBLE else View.GONE
        }

        updateSelctedItem()

        typeArray.recycle()
    }

    fun setLabelName(text: String) {
        binding.tvLabelName.text = text
    }

    fun setTextSpinner(text: String) {
        binding.tvSpinnerValue.text = text
        updateSelctedItem()
    }

    fun getTextSelectedSpinner(): String {
        return binding.tvSpinnerValue.text.toString().trim()
    }

    fun setOnClickInfo(listener: View.OnClickListener) {
        binding.ivInfo.setOnClickListener(listener)
    }

    fun setOnClickSpinner(listener: OnClickListener) {
        binding.tvSpinnerValue.setOnClickListener(listener)
        updateSelctedItem()
    }

    private fun updateSelctedItem() {
        val selectedText = binding.tvSpinnerValue.text.toString()
        if (selectedText == context.resources.getString(R.string.spinner_default_text)) {
            binding.tvSpinnerValue.setTextColor(Color.GRAY)
        } else {
            binding.tvSpinnerValue.setTextColor(Color.BLACK)
        }
    }
}
