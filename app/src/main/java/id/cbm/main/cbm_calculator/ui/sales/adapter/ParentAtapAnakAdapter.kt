package id.cbm.main.cbm_calculator.ui.sales.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.cbm.main.cbm_calculator.databinding.ItemParentAtapAnakBinding
import id.cbm.main.cbm_calculator.ui.sales.model.ParentAtapAnakModel

class ParentAtapAnakAdapter : RecyclerView.Adapter<ParentAtapAnakAdapter.VHAdapterParent>() {

    private val listItemParent: MutableList<ParentAtapAnakModel> = ArrayList()
    fun addItem(item: ParentAtapAnakModel) {
        listItemParent.add(item)
        notifyDataSetChanged()
    }

    fun checkTotalList(): Int {
        return listItemParent.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHAdapterParent {
        return VHAdapterParent(
            ItemParentAtapAnakBinding.inflate(
                LayoutInflater.from(parent.context),
                null,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: VHAdapterParent, position: Int) {
        holder.bind(listItemParent[position])
    }

    override fun getItemCount(): Int {
        return listItemParent.size
    }

    inner class VHAdapterParent(private val binding: ItemParentAtapAnakBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(param: ParentAtapAnakModel) {
            binding.tvTitle.text = param.title

            param.listItemAtap?.let { itemAtap ->
                val adapter = ChildAtapAnakAdapter(itemAtap)
                binding.rvItemAtapAnak.layoutManager = GridLayoutManager(itemView.context, 2)
                binding.rvItemAtapAnak.adapter = adapter
            }
        }
    }
}
