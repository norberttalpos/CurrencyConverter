package hu.bme.aut.currencyconverter.view.list

import androidx.appcompat.app.AppCompatActivity

class CurrencyListActivity : AppCompatActivity() {

/*    private lateinit var binding: ActivityCurrencyListBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

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

        this.initDrawer()

        setContentView(binding.root)
    }

    private fun initDrawer() {
        drawerLayout = binding.drawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item);
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

    override fun onCurrencyClicked(currencyItem: CurrencySelection) {
        if(currencyItem.name != this.baseCurrency.name)
            CoroutineScope(Dispatchers.IO).launch {
                updateBaseCurrency(currencyItem)
                loadCurrencyRates()
            }
    }

    private suspend fun updateBaseCurrency(currencyItem: CurrencySelection) {

        val previous = this.baseCurrency

        withContext(Dispatchers.IO) {
            database.currencySelectionDao().changeBase(previous, currencyItem)

            baseCurrency = database.currencySelectionDao().getBase()!!
        }

        runOnUiThread {
            this.binding.tvBaseCurrency.text = baseCurrency.name
            this.changeBaseCurrencyFlag(baseCurrency)
        }
    }

    private fun changeBaseCurrencyFlag(currencyItem: CurrencySelection) {
        binding.ivBaseCurrency.setImageResource(CurrencyFlagImageGetter.getImageResource(currencyItem.name))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_currency_list -> {
                startActivity(Intent(this, CurrencyListActivity::class.java))
            }
            R.id.nav_currency_select -> {
                startActivity(Intent(this, SelectActivity::class.java))
            }
            R.id.nav_convert -> {
                startActivity(Intent(this, CurrencyListActivity::class.java))
            }
            R.id.nav_prev_conversions -> {
                startActivity(Intent(this, SelectActivity::class.java))
            }
        }
        return true;
    }*/
}