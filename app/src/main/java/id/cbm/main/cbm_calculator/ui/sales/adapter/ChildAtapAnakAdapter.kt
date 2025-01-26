package id.cbm.main.cbm_calculator.ui.sales.adapter // ktlint-disable package-name

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import id.cbm.main.cbm_calculator.databinding.ItemChildAtapAnakEdittextBinding
import id.cbm.main.cbm_calculator.databinding.ItemChildAtapAnakSpinnerBinding
import id.cbm.main.cbm_calculator.ui.sales.model.AtapAnakModel

class ChildAtapAnakAdapter(private val listItem: List<AtapAnakModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return listItem[position].fieldType ?: 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == CHILD_ITEM_VIEW_EDITTEXT) {
            VHAdapterChildEditText(
                ItemChildAtapAnakEdittextBinding.inflate(
                    inflater,
                    null,
                    false,
                ),
            )
        } else {
            VHAdapterChildSpinner(
                ItemChildAtapAnakSpinnerBinding.inflate(
                    inflater,
                    null,
                    false,
                ),
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (listItem[position].fieldType == CHILD_ITEM_VIEW_EDITTEXT) {
            (holder as VHAdapterChildEditText).bind(listItem[position])
        } else {
            (holder as VHAdapterChildSpinner).bind(listItem[position])
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    inner class VHAdapterChildEditText(private val binding: ItemChildAtapAnakEdittextBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(param: AtapAnakModel) {
            binding.etItemAreaAtapditText.setLabelName(param.itemName)
        }
    }

    inner class VHAdapterChildSpinner(private val binding: ItemChildAtapAnakSpinnerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(param: AtapAnakModel) {
            binding.apply {
                etItemAreaAtapditText.setLabelName(param.itemName)
                etItemAreaAtapditText.setOnClickSpinner {
                    Toast.makeText(itemView.context, "Click Child Spinner", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val CHILD_ITEM_VIEW_EDITTEXT = 1
        const val CHILD_ITEM_VIEW_SPINNER = 2
    }
}
