package id.cbm.main.cbm_calculator.core.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.databinding.CustomIconAndTextInfoBinding

class CustomIconWithTextInfo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = CustomIconAndTextInfoBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomIconWithTextInfo)

        val textPosition = typeArray.getInt(R.styleable.CustomIconWithTextInfo_textInfoPosition, 1)
        val textValue = typeArray.getString(R.styleable.CustomIconWithTextInfo_textInfo)
        val iconDrawableSrc = typeArray.getDrawable(R.styleable.CustomIconWithTextInfo_iconSrc)

        if (textPosition == 0) {
            // TOP Position
            binding.tvBottomInfo.visibility = View.GONE

            binding.tvTopInfo.visibility = View.VISIBLE
            binding.tvTopInfo.text = textValue
        } else {
            // BOTTOM Position
            binding.tvTopInfo.visibility = View.GONE

            binding.tvBottomInfo.visibility = View.VISIBLE
            binding.tvBottomInfo.text = textValue
        }

        binding.ivImage.setImageDrawable(iconDrawableSrc)

        typeArray.recycle()
    }
}