package id.cbm.main.cbm_calculator.core.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.databinding.CustomEdittextThreeColumnsBinding
import java.lang.Exception

class CustomEditTextThreeColumns@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr), TextWatcher {

    private val binding = CustomEdittextThreeColumnsBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditTextThreeColumns)

        val labelText = typeArray.getString(R.styleable.CustomEditTextThreeColumns_itemRowsLabel)

        binding.tvLabelItem.text = labelText
        binding.etQ.addTextChangedListener(this)

        typeArray.recycle()
    }

    fun getBeratSatuanValue(): String {
        return binding.etBeratSatuan.text.toString().trim()
    }

    fun getTebalValue(): String {
        return binding.etTebal.text.toString().trim()
    }

    fun getQValue(): String {
        return binding.etQ.text.toString().trim()
    }

    fun setBeratSatuan(result: String) {
        binding.etBeratSatuan.setText(result)
    }

    fun setTebalSatuan(result: String) {
        binding.etTebal.setText(result)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(text: Editable?) {
        try {
            if (binding.etBeratSatuan.text.isNullOrEmpty().not() && binding.etTebal.text.isNullOrEmpty().not()) {
                val berat = binding.etBeratSatuan.text.toString().toDoubleOrNull() ?: 0.0
                val tebal = binding.etTebal.text.toString().toDoubleOrNull() ?: 0.0
                val tempCalculation = berat * tebal

                binding.etQ.setText(tempCalculation.toString())
            }
        } catch (e: Exception) {
            Log.e(CustomEditTextCalculation::class.java.simpleName, "afterTextChanged: err: ${e.message}")
            binding.etQ.setText("0")
        }
    }
}
