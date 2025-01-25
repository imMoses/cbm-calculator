package id.cbm.main.cbm_calculator.ui.sales.adapter // ktlint-disable package-name

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.cbm.main.cbm_calculator.databinding.ItemChildAtapAnakBinding
import id.cbm.main.cbm_calculator.ui.sales.model.AtapAnakModel

class ChildAtapAnakAdapter(private val listItem: List<AtapAnakModel>) : RecyclerView.Adapter<ChildAtapAnakAdapter.VHAdapterChild>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHAdapterChild {
        return VHAdapterChild(
            ItemChildAtapAnakBinding.inflate(
                LayoutInflater.from(parent.context),
                null,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: VHAdapterChild, position: Int) {
        holder.bind(listItem[position])
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    inner class VHAdapterChild(private val binding: ItemChildAtapAnakBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(param: AtapAnakModel) {
            binding.etItemAreaAtapditText.setLabelName(param.itemName)
        }
    }

    companion object {
        const val ITEM_VIEW_EDITTEXT = 1
        const val ITEM_VIEW_SPINNER = 2
    }
}
