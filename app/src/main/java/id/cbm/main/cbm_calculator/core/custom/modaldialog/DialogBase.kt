package id.cbm.main.cbm_calculator.core.custom.modaldialog

import android.app.Dialog
import android.content.DialogInterface.OnDismissListener
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import id.cbm.main.cbm_calculator.R
import id.cbm.main.cbm_calculator.databinding.DialogBottomInfoBinding
import id.cbm.main.cbm_calculator.databinding.DialogBottomListBinding

open class DialogBase : DialogFragment() {

    lateinit var _dialog: Dialog

    val bindingDialogList: DialogBottomListBinding by lazy {
        DialogBottomListBinding.inflate(layoutInflater, null, false)
    }

    val bindingDialogInfo: DialogBottomInfoBinding by lazy {
        DialogBottomInfoBinding.inflate(layoutInflater, null, false)
    }

    private var overrideDismissListener: OnDismissListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _dialog = Dialog(requireContext(), R.style.FullWidth_Dialog)
        return _dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) {
            dismiss()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun initBottomDialogList(overrideDismissListener: OnDismissListener? = null) {
        this.overrideDismissListener = overrideDismissListener

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        _dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        _dialog.window!!.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bg_top_corner_radius))
        _dialog.window!!.setGravity(Gravity.BOTTOM)

        if (bindingDialogList.root.parent != null) {
            (bindingDialogList.root.parent as? ViewGroup)?.removeView(bindingDialogList.root)
        }

        _dialog.setContentView(bindingDialogList.root)
        _dialog.setOnCancelListener {
            if (overrideDismissListener == null) _dialog.dismiss() else overrideDismissListener.onDismiss(_dialog)
        }
    }

    open fun initBottomDialofInfo(overrideDismissListener: OnDismissListener? = null) {
        this.overrideDismissListener = overrideDismissListener
    }

    override fun onDestroyView() {
        _dialog.dismiss()
        super.onDestroyView()
    }
}