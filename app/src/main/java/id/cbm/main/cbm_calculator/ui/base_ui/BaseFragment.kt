package id.cbm.main.cbm_calculator.ui.base_ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<viewBinding : ViewBinding> : Fragment() {

    protected lateinit var _ctx: Context
    private var _binding: viewBinding? = null
    val binding get() = _binding!!

    abstract fun getViewBinding() : viewBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _ctx = requireContext()
        _binding = getViewBinding()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // do something
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            this._ctx = context
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.w("BaseFragment", "onDestroyView triggered!")
        _binding = null
    }
}