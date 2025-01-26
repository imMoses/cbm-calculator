package id.cbm.main.cbm_calculator.ui.sales.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.cbm.main.cbm_calculator.databinding.ItemListMaterialBinding
import id.cbm.main.cbm_calculator.ui.sales.model.HasilEstimasiModel
import id.cbm.main.cbm_calculator.utils.toIDR

class ListMaterialAdapter : ListAdapter<HasilEstimasiModel, ListMaterialAdapter.VHAdapterItem>(ListMaterialDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHAdapterItem {
        return VHAdapterItem(
            ItemListMaterialBinding.inflate(
                LayoutInflater.from(parent.context),
                null,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VHAdapterItem, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VHAdapterItem(private val binding: ItemListMaterialBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(param: HasilEstimasiModel) {
            binding.apply {
                tvMaterialName.text = param.materialName
                tvQty.text = param.qty
                tvharga.text = param.harga.toIDR()
                etDisc.setText("0")
                tvTotal.text = param.total.toIDR(withCurrency = true)
            }
        }
    }

    class ListMaterialDiffCallback : DiffUtil.ItemCallback<HasilEstimasiModel>() {
        override fun areItemsTheSame(
            oldItem: HasilEstimasiModel,
            newItem: HasilEstimasiModel
        ): Boolean {
            return oldItem.materialName == newItem.materialName
        }

        override fun areContentsTheSame(
            oldItem: HasilEstimasiModel,
            newItem: HasilEstimasiModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}