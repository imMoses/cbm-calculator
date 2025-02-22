package id.cbm.main.cbm_calculator.core.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.databinding.CustomToolbarTextAlignBinding

class CustomToolbarTextAlign @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = CustomToolbarTextAlignBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbarTextAlign)

        val toolbarTitleText = typeArray.getString(R.styleable.CustomToolbarTextAlign_text)
        val drawable = typeArray.getDrawable(R.styleable.CustomToolbarTextAlign_navigationIcon)
        val isHideNavBarBackButton = typeArray.getBoolean(R.styleable.CustomToolbarTextAlign_hideNavBackButton, false)
        val showButtonLogout = typeArray.getBoolean(R.styleable.CustomToolbarTextAlign_showButtonLogout, false)

        binding.tvTitleAppBar.text = toolbarTitleText
        if (drawable != null) {
            binding.ivBack.setImageDrawable(drawable)
            binding.ivBack.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }

        binding.ivBack.visibility = if (isHideNavBarBackButton) {
            View.GONE
        } else {
            View.VISIBLE
        }

        binding.ivLogout.visibility = if (showButtonLogout) {
            View.VISIBLE
        } else {
            View.GONE
        }

        typeArray.recycle()
    }

    fun setTile(text: String) {
        binding.tvTitleAppBar.text = text
    }

    fun setNavBackListener(listener: OnClickListener) {
        binding.ivBack.setOnClickListener(listener)
    }

    fun setLogoutButtonListener(listener: OnClickListener) {
        binding.ivLogout.setOnClickListener(listener)
    }

}
