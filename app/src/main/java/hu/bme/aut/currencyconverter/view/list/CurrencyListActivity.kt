package hu.bme.aut.currencyconverter.view.list

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import hu.bme.aut.currencyconverter.data.CurrencyEnum
import hu.bme.aut.currencyconverter.data.CurrencyWithRate
import hu.bme.aut.currencyconverter.data.ListToQueryStringConverter
import hu.bme.aut.currencyconverter.data.repository.CurrencyDatabase
import hu.bme.aut.currencyconverter.data.repository.selection.CurrencySelection
import hu.bme.aut.currencyconverter.databinding.ActivityCurrencyListBinding
import hu.bme.aut.currencyconverter.network.NetworkManager
import hu.bme.aut.currencyconverter.network.response.CurrencyResponse
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyListActivity : AppCompatActivity(), CurrencyListAdapter.CurrencyClickedListener {

    private lateinit var binding: ActivityCurrencyListBinding

    private lateinit var database: CurrencyDatabase
    private lateinit var adapter: CurrencyListAdapter
    private lateinit var swipeContainer: SwipeRefreshLayout

    private lateinit var baseCurrency: CurrencySelection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrencyListBinding.inflate(layoutInflater)

        database = CurrencyDatabase.getDatabase(applicationContext)

        this.initDb()
        this.initRecyclerView()

        swipeContainer = binding.swipeContainer
        swipeContainer.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                loadCurrencyRates()
            }
        }

        setContentView(binding.root)
    }

    private fun initDb() {
        CoroutineScope(Dispatchers.IO).launch {
            initSelectionsInDb()
            loadCurrencyRates()
        }
    }

    private suspend fun initSelectionsInDb() {
        withContext(Dispatchers.IO) {
            val persistedSelections = database.currencySelectionDao().getAll()

            if(persistedSelections.isEmpty()) {
                baseCurrency = CurrencySelection(name = CurrencyEnum.HUF.name, selected = true, base = true)
                CurrencyEnum.values().forEach {
                    if(it.name != CurrencyEnum.HUF.name)
                        database.currencySelectionDao().insert(CurrencySelection(name = it.name, selected = true, base = false))
                    else {
                        database.currencySelectionDao().insert(baseCurrency)
                    }
                }
            }
            else {
                baseCurrency = database.currencySelectionDao().getBase()!!
            }

            runOnUiThread {
                binding.tvBaseCurrency.text = baseCurrency.name
                changeBaseCurrencyFlag(baseCurrency)
            }
        }
    }

    private suspend fun loadCurrencyRates() {
        val toCurrencies: List<CurrencySelection> = withContext(Dispatchers.IO) {
            database.currencySelectionDao().getSelected()
        }

        val toCurrenciesAsString = ListToQueryStringConverter.convertListToQueryString(toCurrencies)

        NetworkManager.getCurrencies(baseCurrency.name, toCurrenciesAsString)?.enqueue(object : Callback<CurrencyResponse?> {
            override fun onResponse(call: Call<CurrencyResponse?>, response: Response<CurrencyResponse?>) {
                Log.d(TAG, "onResponse: " + response.code())
                if (response.isSuccessful) {
                    updateItems(convertResponseToCurrencyItems(response.body()))
                }
            }

            override fun onFailure(call: Call<CurrencyResponse?>, throwable: Throwable) {
                throwable.printStackTrace()
                Toast.makeText(this@CurrencyListActivity, "Network request error occured, check LOG", Toast.LENGTH_LONG).show()
            }
        })

        swipeContainer.isRefreshing = false
    }

    private fun convertResponseToCurrencyItems(response: CurrencyResponse?): List<CurrencyWithRate> {
/*        val items = mutableListOf<CurrencyWithRate>()


        response!!.rates.keys.forEach {
            items.add(CurrencyWithRate(it, response.rates[it]))
        }*/

        return response!!.rates.keys.map {
            CurrencyWithRate(it, response.rates[it])
        }
    }

    private fun initRecyclerView() {
        adapter = CurrencyListAdapter(this)

        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
    }

    private fun updateItems(items: List<CurrencyWithRate>) {
        runOnUiThread {
            adapter.update(items)
        }
    }

    private suspend fun updateBaseCurrency(currencyItem: CurrencySelection) {

        var previous = this.baseCurrency

        withContext(Dispatchers.IO) {
            database.currencySelectionDao().changeBase(previous, currencyItem)

            baseCurrency = database.currencySelectionDao().getBase()!!

            previous = baseCurrency
        }

        runOnUiThread {
            this.binding.tvBaseCurrency.text = baseCurrency.name
            this.changeBaseCurrencyFlag(baseCurrency)
        }
    }

    private fun changeBaseCurrencyFlag(currencyItem: CurrencySelection) {
        binding.ivBaseCurrency.setImageResource(CurrencyListAdapter.getImageResource(currencyItem.name))
    }

    override fun onCurrencyClicked(currencyItem: CurrencySelection) {
        CoroutineScope(Dispatchers.IO).launch {
            updateBaseCurrency(currencyItem)
            loadCurrencyRates()
        }
    }
}