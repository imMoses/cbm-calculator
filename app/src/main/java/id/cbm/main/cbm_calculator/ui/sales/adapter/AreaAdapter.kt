package id.cbm.main.cbm_calculator.ui.sales.adapter // ktlint-disable package-name

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.cbm.main.cbm_calculator.databinding.ItemAdapterEdittextFieldBinding
import id.cbm.main.cbm_calculator.databinding.ItemAdapterSpinnerFieldBinding
import id.cbm.main.cbm_calculator.ui.sales.model.AreaModel

class AreaAdapter(private val listData: List<AreaModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return listData[position].fieldType ?: 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == ITEM_VIEW_EDITTEXT) {
            val view = ItemAdapterEdittextFieldBinding.inflate(inflater, parent, false)
            VHAdapterAreaItemEditText(view)
        } else {
            val view = ItemAdapterSpinnerFieldBinding.inflate(inflater, parent, false)
            VHAdapterAreaItemSpinner(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        listData.let { safeList ->
            if (safeList[position].fieldType == ITEM_VIEW_SPINNER) {
                (holder as VHAdapterAreaItemSpinner).bind(safeList[position])
            } else {
                (holder as VHAdapterAreaItemEditText).bind(safeList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class VHAdapterAreaItemEditText(private val binding: ItemAdapterEdittextFieldBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(param: AreaModel) {
            binding.etItemAreaEditText.setLabelName(param.labelName)
            binding.etItemAreaEditText.setValueText(param.value)
        }
    }

    inner class VHAdapterAreaItemSpinner(private val binding: ItemAdapterSpinnerFieldBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(param: AreaModel) {
            binding.etItemAreaSpinner.setLabelName(param.labelName)
            binding.etItemAreaSpinner.setValueText(param.value)
        }
    }

    companion object {
        const val ITEM_VIEW_EDITTEXT = 1
        const val ITEM_VIEW_SPINNER = 2
    }
}
