package id.cbm.main.cbm_calculator.core.base_ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.databinding.DialogLoadingBinding

abstract class BaseActivity<viewBinding : ViewBinding> : AppCompatActivity() {

    private var _ctx: Context? = null
    private var _binding: viewBinding? = null
    private var dialogLoading: Dialog? = null
    val binding get() = _binding!!
    abstract fun getViewBinding(): viewBinding

    val onBackPressCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onCustomBackPressed()
        }
    }

    fun onCustomBackPressed() {
        dialogLoading?.dismiss()
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _ctx = this
        _binding = getViewBinding()
        setContentView(binding.root)

        // handling back press navigation
        if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                onCustomBackPressed()
            }
        } else {
            onBackPressedDispatcher.addCallback(this, onBackPressCallback)
        }

    }

    fun loadingDialog(customMessage: String = resources.getString(R.string.loading_info)) {
        dialogLoading = Dialog(this)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = DialogLoadingBinding.inflate(inflater)

        dialogLoading?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.tvLoadingMessage.text = customMessage

        dialogLoading?.setContentView(binding.root)
        dialogLoading?.show()
    }

    fun dismissLoading() {
        dialogLoading?.dismiss()
    }
    override fun onDestroy() {
        super.onDestroy()
        _ctx = null
        dismissLoading()
    }
}