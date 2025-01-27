package id.cbm.main.cbm_calculator.core.custom.modaldialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.cbm.main.cbm_calculator.core.custom.modaldialog.adapter.BottomDialogListAdapter
import id.cbm.main.cbm_calculator.core.custom.modaldialog.data.GeneralModel
import id.cbm.main.cbm_calculator.core.custom.modaldialog.interfaces.DialogListClickListener

class DialogList : DialogBase() {

    data class Builder(
        private var title: String = "",
        private var data: MutableList<GeneralModel>? = mutableListOf(),
        private var listener: DialogListClickListener? = null,
        private var overrideDismissListener: DialogInterface.OnDismissListener? = null,
    ) {
        fun title(_title: String) = apply {
            this.title = _title
        }

        fun data(_data: MutableList<GeneralModel>) = apply {
            this.data = _data
        }

        fun listener(listener: DialogListClickListener?) = apply {
            this.listener = listener
        }

        fun overrideDismissListener(_listener: DialogInterface.OnDismissListener) = apply {
            this.overrideDismissListener = _listener
        }

        fun build(): DialogList {
            val dialogList = DialogList()
            dialogList.setData(
                title = title,
                data = data,
                listener = listener,
                overrideDismissListener = overrideDismissListener,
            )
            return dialogList
        }
    }

    private var title: String = ""
    private var data: MutableList<GeneralModel>? = mutableListOf()
    private var listener: DialogListClickListener? = null
    private var overrideDismissListener: DialogInterface.OnDismissListener? = null

    fun setData(
        title: String = "",
        data: MutableList<GeneralModel>? = mutableListOf(),
        listener: DialogListClickListener? = null,
        overrideDismissListener: DialogInterface.OnDismissListener? = null
    ) {
        this.title = title
        this.data = data
        this.listener = listener
        this.overrideDismissListener = overrideDismissListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        initBottomDialogList(overrideDismissListener)
        setDataDialog(data ?: listOf())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        bindingDialogList.tvTitleDialog.text = title
        bindingDialogList.rvItems.layoutManager = LinearLayoutManager(requireContext())
    }

    fun updateData(data: List<GeneralModel>) {
        if (data.isNotEmpty()) {
            setDataDialog(data)
        }
    }

    private fun setDataDialog(data: List<GeneralModel>, position: Int = -1) {
        val bottomListAdapter = BottomDialogListAdapter(
            object : BottomDialogListAdapter.BottomDialogListListener {
                override fun onClickItem(data: GeneralModel) {
                    listener?.itemClick(data)
                    _dialog.dismiss()
                }
            }
        )
        bottomListAdapter.submitList(data)
        bindingDialogList.rvItems.adapter = bottomListAdapter
    }
}