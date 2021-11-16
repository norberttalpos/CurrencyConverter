package hu.bme.aut.currencyconverter.view.pages.previous


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.currencyconverter.data.repository.conversion.Conversion
import hu.bme.aut.currencyconverter.databinding.ItemPreviousConversionListBinding
import hu.bme.aut.currencyconverter.view.getImageResource

class PreviousConversionsListAdapter : RecyclerView.Adapter<PreviousConversionsListAdapter.PreviousConversionsListItemViewHolder>() {

    inner class PreviousConversionsListItemViewHolder(val binding: ItemPreviousConversionListBinding) : RecyclerView.ViewHolder(binding.root)

    private var items = mutableListOf<Conversion>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousConversionsListItemViewHolder = PreviousConversionsListItemViewHolder(
        ItemPreviousConversionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: PreviousConversionsListItemViewHolder, position: Int) {
        val conversionItem = items[position]

        holder.binding.ivFlagFrom.setImageResource(getImageResource(conversionItem.from.name))
        holder.binding.ivFlagTo.setImageResource(getImageResource(conversionItem.to.name))

        holder.binding.tvNameFrom.text = conversionItem.from.name
        holder.binding.tvNameTo.text = conversionItem.to.name

        holder.binding.tvAmount.text = conversionItem.amount.toString()
        holder.binding.tvResult.text = conversionItem.result.toString()
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<Conversion>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
