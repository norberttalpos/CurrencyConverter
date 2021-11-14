package hu.bme.aut.currencyconverter.view.list

import android.content.ContentValues.TAG
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.currencyconverter.data.CurrencyEnum
import hu.bme.aut.currencyconverter.data.CurrencyWithRate
import hu.bme.aut.currencyconverter.data.ListToQueryStringConverter
import hu.bme.aut.currencyconverter.data.repository.CurrencyDatabase
import hu.bme.aut.currencyconverter.data.repository.selection.CurrencyName
import hu.bme.aut.currencyconverter.databinding.ActivityCurrencyListBinding
import hu.bme.aut.currencyconverter.network.NetworkManager
import hu.bme.aut.currencyconverter.network.response.CurrencyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class CurrencyListActivity : AppCompatActivity(), CurrencyListAdapter.CurrencyClickedListener {

    private lateinit var binding: ActivityCurrencyListBinding

    private lateinit var database: CurrencyDatabase
    private lateinit var adapter: CurrencyListAdapter

    private lateinit var baseCurrency: CurrencyName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrencyListBinding.inflate(layoutInflater)

        baseCurrency = CurrencyName(name = CurrencyEnum.CHF.name)

        binding.tvBaseCurrency.text = baseCurrency.name

        database = CurrencyDatabase.getDatabase(applicationContext)

        this.initDb()
        this.initRecyclerView()
        this.changeBaseCurrencyFlag(this.baseCurrency)

        setContentView(binding.root)
    }

    private fun initDb() {
        GlobalScope.launch {
            initSelectionsInDb()
            fetchItems()
        }
    }

    private suspend fun initSelectionsInDb() {
        //TODO csak az eltaroltat

        withContext(Dispatchers.IO) {
            database.currencySelectionDao().deleteAll()

            CurrencyEnum.values().forEach {
                if (it.name != baseCurrency.name)
                    database.currencySelectionDao().insert(CurrencyName(name = it.name))
            }
        }
    }

    private suspend fun loadCurrencyRates() {
        val toCurrencies: List<CurrencyName> = withContext(Dispatchers.IO) {
            database.currencySelectionDao().getAll()
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
    }

    private fun convertResponseToCurrencyItems(response: CurrencyResponse?): List<CurrencyWithRate> {
        val items = mutableListOf<CurrencyWithRate>()

        response!!.rates.keys.forEach {
            items.add(CurrencyWithRate(it, response.rates[it]))
        }

        return items
    }

    private fun initRecyclerView() {
        adapter = CurrencyListAdapter(this)

        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
    }

    private suspend fun fetchItems() {
        val items = mutableListOf<CurrencyWithRate>()

        database.currencySelectionDao().getAll().forEach {
            items.add(CurrencyWithRate(name = it.name, rate = 100.0))
        }

        this.loadCurrencyRates()
    }

    private fun updateItems(items: List<CurrencyWithRate>) {
        runOnUiThread {
            adapter.update(items)
        }
    }

    private suspend fun updateBaseCurrency(currencyItem: CurrencyName) {

        val previous = this.baseCurrency
        this.baseCurrency = currencyItem
        runOnUiThread {
            this.binding.tvBaseCurrency.text = currencyItem.name
        }

        withContext(Dispatchers.IO) {
            database.currencySelectionDao().changeBase(previous, currencyItem)
        }
    }

    private fun changeBaseCurrencyFlag(currencyItem: CurrencyName) {
        binding.ivBaseCurrency.setImageResource(CurrencyListAdapter.getImageResource(currencyItem.name))
    }

    override fun onCurrencyClicked(currencyItem: CurrencyName) {
        GlobalScope.launch {
            updateBaseCurrency(currencyItem)
            fetchItems()
        }
        this.changeBaseCurrencyFlag(currencyItem)
    }
}