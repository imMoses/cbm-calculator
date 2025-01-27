package id.cbm.main.cbm_calculator.core.custom.modaldialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.cbm.main.cbm_calculator.core.custom.modaldialog.data.GeneralModel
import id.cbm.main.cbm_calculator.databinding.ItemBottomDialogListAdapterBinding

class BottomDialogListAdapter(
    private val listener: BottomDialogListListener,
) : ListAdapter<GeneralModel, BottomDialogListAdapter.VHAdapterBottomDialogList>(BotDialogListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHAdapterBottomDialogList {
        return VHAdapterBottomDialogList(
            ItemBottomDialogListAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                null,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: VHAdapterBottomDialogList, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VHAdapterBottomDialogList(private val binding: ItemBottomDialogListAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GeneralModel) {
            binding.tvText.text = item.itemName

            binding.lyParent.setOnClickListener {
                listener.onClickItem(item)
            }
        }
    }

    class BotDialogListDiffCallback : DiffUtil.ItemCallback<GeneralModel>() {
        override fun areItemsTheSame(oldItem: GeneralModel, newItem: GeneralModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GeneralModel, newItem: GeneralModel): Boolean {
            return oldItem == newItem
        }
    }

    interface BottomDialogListListener {
        fun onClickItem(data: GeneralModel)
    }
}