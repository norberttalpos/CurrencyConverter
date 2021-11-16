package hu.bme.aut.currencyconverter.view.pages.conversion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import hu.bme.aut.currencyconverter.data.CurrencyEnum
import hu.bme.aut.currencyconverter.data.repository.CurrencyDatabase
import hu.bme.aut.currencyconverter.data.repository.conversion.Conversion
import hu.bme.aut.currencyconverter.databinding.FragmentConversionBinding
import hu.bme.aut.currencyconverter.network.NetworkManager
import hu.bme.aut.currencyconverter.network.response.CurrencyConversionResponse
import hu.bme.aut.currencyconverter.view.pages.conversion.dialog.ConversionDialogFragment
import hu.bme.aut.currencyconverter.view.getImageResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ConversionFragment : Fragment(), ConversionDialogFragment.CurrencySelectedOnDialogListener {

    private lateinit var binding: FragmentConversionBinding

    private lateinit var database: CurrencyDatabase

    private lateinit var textFieldAmount: TextInputLayout
    private lateinit var textResult: TextView

    private lateinit var buttonFrom: Button
    private lateinit var buttonTo: Button

    private lateinit var iconFrom: ImageView
    private lateinit var textFrom: TextView
    private lateinit var iconTo: ImageView
    private lateinit var textTo: TextView

    private lateinit var buttonConvert: Button

    private lateinit var selectLayout: LinearLayout

    private var amount: Int = 0

    private var fromCurrency = CurrencyEnum.EUR
    private var toCurrency = CurrencyEnum.HUF

    private var activeDialog: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentConversionBinding.inflate(inflater, container, false)

        database = CurrencyDatabase.getDatabase(requireActivity().applicationContext)

        buttonFrom = binding.textButtonFrom
        buttonTo = binding.textButtonTo
        buttonConvert = binding.textButtonConvert

        iconFrom = binding.ivFlagFrom
        textFrom = binding.tvNameFrom
        iconTo = binding.ivFlagTo
        textTo = binding.tvNameTo

        textFieldAmount = binding.textFieldAmount

        textResult = binding.tvResult
        textResult.text = this.amount.toString()

        selectLayout = binding.conversionLayout

        selectLayout.setOnClickListener {
            this.hideKeyBoard()
        }

        this.initDialogTogglerButtons()
        this.initConvertButton()
        this.initAmountTextField()
        this.initCurrencyViews()

        return binding.root
    }

    private fun initDialogTogglerButtons() {
        buttonFrom.setOnClickListener {
            this.activeDialog = 0

            this.openDialog()
        }

        buttonTo.setOnClickListener {
            this.activeDialog = 1

            this.openDialog()
        }
    }

    private fun openDialog() {
        ConversionDialogFragment(this).show(
            requireActivity().supportFragmentManager,
            "tag"
        )
    }

    private fun initCurrencyViews() {
        this.changeCurrencyView(fromCurrency, this.iconFrom, this.textFrom)
        this.changeCurrencyView(toCurrency, this.iconTo, this.textTo)
    }

    private fun initConvertButton() {
        buttonConvert.setOnClickListener {

            if(fromCurrency == toCurrency) {
                setResult(this.amount.toDouble())
                saveConversionToDb(Conversion(from = fromCurrency, to = toCurrency, amount = amount, result = this.amount.toDouble()))

                return@setOnClickListener
            }

            NetworkManager.getConversion(fromCurrency.name, toCurrency.name, this.amount.toDouble())?.enqueue(object : Callback<CurrencyConversionResponse?> {

                override fun onResponse(call: Call<CurrencyConversionResponse?>, response: Response<CurrencyConversionResponse?>) {
                    if (response.isSuccessful) {
                        val result = response.body()!!.rates[toCurrency.name]!!.toDouble()

                        setResult(result)
                        saveConversionToDb(Conversion(from = fromCurrency, to = toCurrency, amount = amount, result = result))
                    }
                }

                override fun onFailure(call: Call<CurrencyConversionResponse?>, throwable: Throwable) {
                    throwable.printStackTrace()
                    Toast.makeText(requireContext(), "Network request error occured, check LOG", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun initAmountTextField() {

        textFieldAmount.editText?.doOnTextChanged { inputText, _, _, _ ->
            val parsed = inputText.toString().toIntOrNull()
            this.amount = parsed ?: 0
        }
    }

    override fun onCurrencySelected(currencyItem: CurrencyEnum) {
        if(this.activeDialog == 0) {
            fromCurrency = currencyItem
            this.changeCurrencyView(currencyItem, this.iconFrom, this.textFrom)
        }
        else if(this.activeDialog == 1) {
            toCurrency = currencyItem
            this.changeCurrencyView(currencyItem, this.iconTo, this.textTo)
        }
        else {
            throw IllegalStateException()
        }

        this.activeDialog = null
    }

    private fun changeCurrencyView(currency: CurrencyEnum, icon: ImageView, text: TextView) {
        icon.setImageResource(getImageResource(currency.name))
        text.text = currency.name
    }

    private fun setResult(result: Double) {
        this.textResult.text = result.toString()
    }

    private fun saveConversionToDb(conversion: Conversion) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                database.conversionListDao().insert(conversion)
            }
        }
    }

    private fun hideKeyBoard() {
        selectLayout.hideKeyboard()
        binding.tvResult.requestFocus()
    }

    private fun View.hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
    }
}