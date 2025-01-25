package id.cbm.main.cbm_calculator.core.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import id.cbm.main.cbm_calculator.databinding.CbmSpinnertextBinding

class CBMSpinnerText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = CbmSpinnertextBinding.inflate(LayoutInflater.from(context), this, true)

}