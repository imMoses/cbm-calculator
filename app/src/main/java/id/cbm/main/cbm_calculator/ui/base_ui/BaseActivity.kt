package id.cbm.main.cbm_calculator.ui.base_ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<viewBinding : ViewBinding> : AppCompatActivity() {

    private var _ctx: Context? = null
    private var _binding: viewBinding? = null
    val binding get() = _binding!!
    abstract fun getViewBinding(): viewBinding

    val onBackPressCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onCustomBackPressed()
        }
    }

    fun onCustomBackPressed() {
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
    override fun onDestroy() {
        super.onDestroy()
        _ctx = null
    }
}