package hu.bme.aut.currencyconverter.view.fragments.select

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.currencyconverter.data.repository.selection.CurrencySelection
import hu.bme.aut.currencyconverter.databinding.ItemSelectListBinding
import hu.bme.aut.currencyconverter.view.getImageResource

class SelectListAdapter(private val listener: SelectionCurrencyClickedListener): RecyclerView.Adapter<SelectListAdapter.SelectListItemViewHolder>() {

    inner class SelectListItemViewHolder(val binding: ItemSelectListBinding) : RecyclerView.ViewHolder(binding.root)

    private val items = mutableListOf<CurrencySelection>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectListItemViewHolder = SelectListItemViewHolder(
        ItemSelectListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SelectListItemViewHolder, position: Int) {
        val currencyItem = items[position]

        holder.binding.ivFlag.setImageResource(getImageResource(currencyItem.name))
        holder.binding.tvName.text = currencyItem.name
        holder.binding.cbSelected.isChecked = currencyItem.selected

        holder.binding.cbSelected.setOnClickListener {
            currencyItem.selected = !currencyItem.selected
            listener.onSelectionToggled(currencyItem)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(currencyNameSelection: List<CurrencySelection>) {
        items.clear()
        items.addAll(currencyNameSelection)
        notifyDataSetChanged()
    }

    interface SelectionCurrencyClickedListener {
        fun onSelectionToggled(currencyItem: CurrencySelection)
    }
}
