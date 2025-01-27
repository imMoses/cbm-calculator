package id.cbm.main.cbm_calculator.ui.sales.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.cbm.main.cbm_calculator.databinding.ItemListMaterialBinding
import id.cbm.main.cbm_calculator.ui.sales.model.HasilEstimasiModel
import id.cbm.main.cbm_calculator.utils.CustomRegex.ZERO_FIRST_NOT_ALLOWED
import id.cbm.main.cbm_calculator.utils.toIDR
import java.lang.Exception
import java.util.Locale

class ListMaterialAdapter : ListAdapter<HasilEstimasiModel, ListMaterialAdapter.VHAdapterItem>(ListMaterialDiffCallback()) {

    private var listenerListMaterial: ListMaterialListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHAdapterItem {
        return VHAdapterItem(
            ItemListMaterialBinding.inflate(
                LayoutInflater.from(parent.context),
                null,
                false,
            ),
        )
    }

    fun onUpdateTotalPrice(listener: ListMaterialListener) {
        this.listenerListMaterial = listener
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
                tvTotal.text = calculateTotal(
                    qty = param.qty.toLong(),
                    harga = param.harga.toLong(),
                ).toIDR(withCurrency = true)

                etDisc.addTextChangedListener(
                    etDisc.sanitizeDiscFieldInput(
                        qty = param.qty.toLong(),
                        harga = param.harga.toLong(),
                        param = param,
                        tvTotal = tvTotal,
                    ),
                )
            }
        }

        private fun calculateTotal(qty: Long, harga: Long): String {
            return try {
                val result = qty * harga
                result.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(ListMaterialAdapter::class.simpleName, "calculateTotal err: ${e.message ?: "-"}")
                "0"
            }
        }

        private fun EditText.sanitizeDiscFieldInput(qty: Long, harga: Long, param: HasilEstimasiModel, tvTotal: TextView): TextWatcher {
            val listener = object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    text?.let { safeText ->
                        if (safeText.length > 1) {
                            val regex = ZERO_FIRST_NOT_ALLOWED.toRegex()
                            if (text.contains(regex)) {
                                val cleanText = safeText.replace(regex, "")
                                this@sanitizeDiscFieldInput.setText(cleanText)
                                this@sanitizeDiscFieldInput.setSelection(cleanText.length)
                            }
                        }
                    }
                }

                override fun afterTextChanged(inputDisc: Editable?) {
                    try {
                        if (!inputDisc.isNullOrBlank()) {
                            val disc = inputDisc.toString().toDouble()
                            val mainTotal = calculateTotal(qty, harga).toDouble()

                            val discResult = (mainTotal * disc) / 100.0
                            val finalResult = mainTotal - discResult
                            if (finalResult > 0.0) {
                                val finalResultString = String.format(Locale.US, "%.2f", finalResult)
                                tvTotal.text = finalResultString.toIDR(withCurrency = true)
                                param.total = finalResultString
                            } /*else {
                                tvTotal.text = "0".toIDR(withCurrency = true)
                                param.total = "0"
                            }*/
                        }

                        listenerListMaterial?.onUpdateTotalPrice(currentList)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e(ListMaterialAdapter::class.simpleName, "sanitizeDiscFieldInput.afterTextChanged err: ${e.message ?: "-"}")
                        tvTotal.text = calculateTotal(qty, harga).toIDR(withCurrency = true)
                    }
                }
            }

            return listener
        }
    }

    class ListMaterialDiffCallback : DiffUtil.ItemCallback<HasilEstimasiModel>() {
        override fun areItemsTheSame(
            oldItem: HasilEstimasiModel,
            newItem: HasilEstimasiModel,
        ): Boolean {
            return oldItem.materialName == newItem.materialName
        }

        override fun areContentsTheSame(
            oldItem: HasilEstimasiModel,
            newItem: HasilEstimasiModel,
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface ListMaterialListener {
        fun onUpdateTotalPrice(totalPrices: MutableList<HasilEstimasiModel>)
    }
}
