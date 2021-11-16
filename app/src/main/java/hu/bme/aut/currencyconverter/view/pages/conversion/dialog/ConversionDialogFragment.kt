package hu.bme.aut.currencyconverter.view.pages.conversion.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import hu.bme.aut.currencyconverter.data.CurrencyEnum
import hu.bme.aut.currencyconverter.databinding.FragmentConversionDialogBinding

class ConversionDialogFragment(private val listener: CurrencySelectedOnDialogListener) : DialogFragment(),
    ConversionDialogListAdapter.DialogListClickedListener {

    interface CurrencySelectedOnDialogListener {
        fun onCurrencySelected(currencyItem: CurrencyEnum)
    }

    private lateinit var binding: FragmentConversionDialogBinding

    private lateinit var adapter: ConversionDialogListAdapter


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentConversionDialogBinding.inflate(LayoutInflater.from(context))

        this.initRecyclerView()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choose currency")
            .setView(binding.root)
            .create()
    }

    private fun initRecyclerView() {
        adapter = ConversionDialogListAdapter(this)

        binding.rvDialog.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDialog.adapter = adapter

        adapter.setItems(CurrencyEnum.values().toList())
    }

    override fun onCurrencyClicked(currencyItem: CurrencyEnum) {
        listener.onCurrencySelected(currencyItem)

        this.dismiss()
    }
}