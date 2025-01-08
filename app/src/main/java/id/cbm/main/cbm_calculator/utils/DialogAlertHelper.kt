package id.cbm.main.cbm_calculator.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import id.cbm.main.cbm_calculator.R

class DialogAlertHelper {

    companion object {

        fun showDialogMessage(context: Context?, message: String, title: String?, listener: DialogInfoListener?) {
            val dialog = Dialog(context!!)
            val view = View.inflate(context, R.layout.dialog_alert_info, null)
            val textTitle = view.findViewById<TextView>(R.id.textTitle)
            val textMessage = view.findViewById<TextView>(R.id.textMessage)
            val btnOk = view.findViewById<View>(R.id.textOk)

            if (title != null) textTitle.text = title
            textMessage.text = message
            btnOk.setOnClickListener {
                dialog.dismiss()
                listener?.onClickOk()
            }

            dialog.setContentView(view)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.create()
            dialog.show()
        }
    }

    interface DialogInfoListener {
        fun onClickOk()
    }

    interface DialogInfoDecisionListener {
        fun onPositive()
        fun onNegative()
    }
}
