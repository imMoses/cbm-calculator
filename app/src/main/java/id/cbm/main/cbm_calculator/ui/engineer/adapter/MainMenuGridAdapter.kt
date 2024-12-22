package id.cbm.main.cbm_calculator.ui.engineer.adapter // ktlint-disable package-name

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.cbm.main.cbm_calculator.databinding.ItemMenuEngineerBinding
import id.cbm.main.cbm_calculator.utils.setSafeOnClickListener

class MainMenuGridAdapter(private val listItem: List<MenuEngineerItem>, private val listener: MainMenuGridAdapterListener) : RecyclerView.Adapter<MainMenuGridAdapter.VHAdapterItemMenu>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHAdapterItemMenu {
        return VHAdapterItemMenu(
            ItemMenuEngineerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: VHAdapterItemMenu, position: Int) {
        holder.bind(listItem[position])
    }

    override fun getItemCount() = listItem.size

    inner class VHAdapterItemMenu(private val binding: ItemMenuEngineerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuEngineerItem) {
            binding.tvRequestForm.text = item.name
            binding.ivRequestForm.setImageDrawable(item.icon)
            itemView.setSafeOnClickListener {
                listener.onClickItem(item)
            }
        }
    }

    interface MainMenuGridAdapterListener {
        fun onClickItem(item: MenuEngineerItem)
    }
}

data class MenuEngineerItem(
    var id: String,
    var name: String? = null,
    var icon: Drawable? = null,
)
