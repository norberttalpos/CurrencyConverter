package hu.bme.aut.currencyconverter.view.fragments.conversion.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.currencyconverter.data.CurrencyEnum
import hu.bme.aut.currencyconverter.databinding.ItemDialogListBinding
import hu.bme.aut.currencyconverter.view.getImageResource

class ConversionDialogListAdapter(private val listener: DialogListClickedListener): RecyclerView.Adapter<ConversionDialogListAdapter.DialogListItemViewHolder>() {

    inner class DialogListItemViewHolder(val binding: ItemDialogListBinding) : RecyclerView.ViewHolder(binding.root)

    private var items = listOf<CurrencyEnum>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogListItemViewHolder = DialogListItemViewHolder(
        ItemDialogListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: DialogListItemViewHolder, position: Int) {
        val currencyItem = items[position]

        holder.binding.ivFlagDialog.setImageResource(getImageResource(currencyItem.name))
        holder.binding.tvNameDialog.text = currencyItem.name

        holder.binding.dialogRow.setOnClickListener {
            listener.onCurrencyClicked(currencyItem)
        }
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<CurrencyEnum>) {
        this.items = items
    }

    interface DialogListClickedListener {
        fun onCurrencyClicked(currencyItem: CurrencyEnum)
    }
}
