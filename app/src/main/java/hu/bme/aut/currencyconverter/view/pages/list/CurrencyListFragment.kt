package hu.bme.aut.currencyconverter.view.pages.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import hu.bme.aut.currencyconverter.data.CurrencyWithRate
import hu.bme.aut.currencyconverter.data.convertListToQueryString
import hu.bme.aut.currencyconverter.data.repository.CurrencyDatabase
import hu.bme.aut.currencyconverter.data.repository.selection.CurrencySelection
import hu.bme.aut.currencyconverter.databinding.FragmentCurrencyListBinding
import hu.bme.aut.currencyconverter.network.NetworkManager
import hu.bme.aut.currencyconverter.network.response.CurrencyResponse
import hu.bme.aut.currencyconverter.view.getImageResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyListFragment : Fragment(), CurrencyListAdapter.CurrencyClickedListener {
    private lateinit var binding: FragmentCurrencyListBinding

    private lateinit var database: CurrencyDatabase
    private lateinit var adapter: CurrencyListAdapter
    private lateinit var swipeContainer: SwipeRefreshLayout

    private lateinit var rootContainer: LinearLayout
    private lateinit var progressBar: ProgressBar

    private lateinit var baseCurrency: CurrencySelection

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCurrencyListBinding.inflate(inflater, container, false)

        database = CurrencyDatabase.getDatabase(requireActivity().applicationContext)

        rootContainer = binding.rootContainer
        rootContainer.isVisible = false

        progressBar = binding.progressBar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initRecyclerView()

        swipeContainer = binding.swipeContainer
        swipeContainer.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                loadCurrencyRates()
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            initBaseCurrency()
            loadCurrencyRates()
        }
    }

    private suspend fun initBaseCurrency() {
        withContext(Dispatchers.IO) {
            var baseCurrencyInDb = database.currencySelectionDao().getBase()
            if(baseCurrencyInDb == null) {
                database.currencySelectionDao().makeNewBase()
                baseCurrencyInDb = database.currencySelectionDao().getBase()

                baseCurrency = baseCurrencyInDb!!
            }
            else
                baseCurrency = baseCurrencyInDb
        }

        requireActivity().runOnUiThread {
            binding.tvBaseCurrency.text = baseCurrency.name
            changeBaseCurrencyFlag(baseCurrency)
        }
    }

    private suspend fun loadCurrencyRates() {
        val toCurrencies: List<CurrencySelection> = withContext(Dispatchers.IO) {
            database.currencySelectionDao().getSelected()
        }

        val toCurrenciesAsString = convertListToQueryString(toCurrencies)

        NetworkManager.getCurrencies(baseCurrency.name, toCurrenciesAsString)?.enqueue(object : Callback<CurrencyResponse?> {

            override fun onResponse(call: Call<CurrencyResponse?>, response: Response<CurrencyResponse?>) {
                if (response.isSuccessful) {
                    updateItems(convertResponseToCurrencyItems(response.body()))
                }
            }

            override fun onFailure(call: Call<CurrencyResponse?>, throwable: Throwable) {
                throwable.printStackTrace()
                Toast.makeText(requireContext(), "Network request error occured, check LOG", Toast.LENGTH_LONG).show()
            }
        })

        swipeContainer.isRefreshing = false
    }

    private fun convertResponseToCurrencyItems(response: CurrencyResponse?): List<CurrencyWithRate> {

        return response!!.rates.keys.map {
            CurrencyWithRate(it, response.rates[it])
        }
    }

    private fun initRecyclerView() {
        adapter = CurrencyListAdapter(this)

        binding.rvMain.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMain.adapter = adapter
    }

    private fun updateItems(items: List<CurrencyWithRate>) {
        requireActivity().runOnUiThread {
            loadingEnded()

            adapter.update(items)
        }
    }

    private fun loadingEnded() {
        progressBar.isVisible = false
        rootContainer.isVisible = true
    }

    override fun onCurrencyClicked(currencyItem: CurrencySelection) {
        if(currencyItem.name != this.baseCurrency.name)
            progressBar.isVisible = true
            CoroutineScope(Dispatchers.IO).launch {
                updateBaseCurrency(currencyItem)
                loadCurrencyRates()
                updateBaseCurrencyView()
            }
    }

    private suspend fun updateBaseCurrency(currencyItem: CurrencySelection) {
        val previous = this.baseCurrency

        withContext(Dispatchers.IO) {
            database.currencySelectionDao().changeBase(previous, currencyItem)

            baseCurrency = database.currencySelectionDao().getBase()!!
        }
    }

    private fun updateBaseCurrencyView() {
        requireActivity().runOnUiThread {
            binding.tvBaseCurrency.text = baseCurrency.name
            changeBaseCurrencyFlag(baseCurrency)
        }
    }

    private fun changeBaseCurrencyFlag(currencyItem: CurrencySelection) {
        binding.ivBaseCurrency.setImageResource(getImageResource(currencyItem.name))
    }
}