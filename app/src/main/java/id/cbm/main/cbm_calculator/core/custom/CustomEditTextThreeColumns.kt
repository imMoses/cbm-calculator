package id.cbm.main.cbm_calculator.core.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.databinding.CustomEdittextThreeColumnsBinding
import org.w3c.dom.Text
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
        val enableBerat = typeArray.getBoolean(R.styleable.CustomEditTextThreeColumns_enableBeratSatuan, true)
        val enableTebal = typeArray.getBoolean(R.styleable.CustomEditTextThreeColumns_enableTebal, false)
        val enableQ = typeArray.getBoolean(R.styleable.CustomEditTextThreeColumns_enableQ, false)

        binding.tvLabelItem.text = labelText
        binding.etBeratSatuan.isEnabled = enableBerat
        binding.etTebal.isEnabled = enableTebal
        binding.etQ.isEnabled = enableQ
        binding.etTebal.addTextChangedListener(this)

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

    fun setValueQ(result: String) {
        binding.etQ.setText(result)
    }

    fun getBeratSatuanEditText() : EditText {
        return binding.etBeratSatuan
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(text: Editable?) {
        try {
            if (binding.etBeratSatuan.text.isNullOrEmpty().not() && binding.etTebal.text.isNullOrEmpty().not()) {
                if (binding.etTebal.text.toString().equals("-")) {
                    val berat = binding.etBeratSatuan.text.toString().toDoubleOrNull() ?: 0.0

                    binding.etQ.setText(berat.toString())
                } else {
                    val berat = binding.etBeratSatuan.text.toString().toDoubleOrNull() ?: 0.0
                    val tebal = binding.etTebal.text.toString().toDoubleOrNull() ?: 0.0
                    val tempCalculation = berat * tebal

                    binding.etQ.setText(tempCalculation.toString())
                }
            } else {
                binding.etQ.setText("0.0")
            }
        } catch (e: Exception) {
            Log.e(CustomEditTextCalculation::class.java.simpleName, "afterTextChanged: err: ${e.message}")
            binding.etQ.setText("0")
        }
    }
}
