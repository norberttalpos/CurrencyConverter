package hu.bme.aut.currencyconverter.view.list;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.currencyconverter.R
import hu.bme.aut.currencyconverter.data.CurrencyEnum
import hu.bme.aut.currencyconverter.data.CurrencyWithRate
import hu.bme.aut.currencyconverter.data.repository.selection.CurrencyName
import hu.bme.aut.currencyconverter.databinding.ItemCurrencyListBinding

class CurrencyListAdapter(private val listener: CurrencyClickedListener): RecyclerView.Adapter<CurrencyListAdapter.CurrencyListItemViewHolder>() {

    inner class CurrencyListItemViewHolder(val binding: ItemCurrencyListBinding) : RecyclerView.ViewHolder(binding.root)

    private val items = mutableListOf<CurrencyWithRate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListItemViewHolder = CurrencyListItemViewHolder(
        ItemCurrencyListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CurrencyListItemViewHolder, position: Int) {
        val currencyItem = items[position]

        holder.binding.ivFlag.setImageResource(getImageResource(currencyItem.name))
        holder.binding.tvName.text = currencyItem.name
        holder.binding.tvRate.text = currencyItem.rate.toString()

        holder.binding.currencyRow.setOnClickListener {
            listener.onCurrencyClicked(CurrencyName(name = currencyItem.name))
        }
    }

    companion object {
        @DrawableRes()
        fun getImageResource(name: String): Int {
            return when (name) {
                CurrencyEnum.HUF.name -> R.drawable.huf
                CurrencyEnum.USD.name -> R.drawable.usd
                CurrencyEnum.EUR.name -> R.drawable.eur
                CurrencyEnum.CHF.name -> R.drawable.chf
                CurrencyEnum.GBP.name -> R.drawable.gbp
                else -> R.drawable.empty
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(currencyNameSelection: List<CurrencyWithRate>) {
        items.clear()
        items.addAll(currencyNameSelection)
        notifyDataSetChanged()
    }

    interface CurrencyClickedListener {
        fun onCurrencyClicked(currencyItem: CurrencyName)
    }
}
