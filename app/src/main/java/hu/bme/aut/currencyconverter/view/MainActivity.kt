package hu.bme.aut.currencyconverter.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.currencyconverter.R
import hu.bme.aut.currencyconverter.data.CurrencyEnum
import hu.bme.aut.currencyconverter.data.repository.CurrencyDatabase
import hu.bme.aut.currencyconverter.data.repository.selection.CurrencySelection
import hu.bme.aut.currencyconverter.databinding.ActivityMainBinding
import hu.bme.aut.currencyconverter.view.fragments.conversion.ConversionFragment
import hu.bme.aut.currencyconverter.view.fragments.list.CurrencyListFragment
import hu.bme.aut.currencyconverter.view.fragments.select.SelectListFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var database: CurrencyDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        database = CurrencyDatabase.getDatabase(applicationContext)

        this.initDrawer()

        this.initDb()

        setContentView(binding.root)
    }

    private fun initDb() {
        CoroutineScope(Dispatchers.IO).launch {
            initSelectionsInDb()
        }
    }

    private suspend fun initSelectionsInDb() {
        withContext(Dispatchers.IO) {
            val persistedSelections = database.currencySelectionDao().getAll()

            if(persistedSelections.isEmpty()) {
                val baseCurrency = CurrencySelection(name = CurrencyEnum.HUF.name, selected = true, base = true)

                var nbSelectedInserted = 0
                CurrencyEnum.values().forEach {
                    if(it.name != CurrencyEnum.HUF.name && nbSelectedInserted < 6) {
                        database.currencySelectionDao().insert(CurrencySelection(name = it.name, selected = true, base = false))
                        ++nbSelectedInserted
                    }
                    else if(it.name != CurrencyEnum.HUF.name) {
                        database.currencySelectionDao().insert(CurrencySelection(name = it.name, selected = false, base = false))
                    }
                    else {
                        database.currencySelectionDao().insert(baseCurrency)
                        ++nbSelectedInserted
                    }
                }
            }

            onDbInitEnded()
        }
    }

    private fun onDbInitEnded() {
        supportFragmentManager.beginTransaction().replace(R.id.flContent, CurrencyListFragment()).commit()
    }

    private fun initDrawer() {
        drawerLayout = binding.drawerLayout
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_currency_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.nav_currency_list -> {
                fragment = CurrencyListFragment()
            }
            R.id.nav_currency_select -> {
                fragment = SelectListFragment()
            }
            R.id.nav_convert -> {
                fragment = ConversionFragment()
            }
            R.id.nav_prev_conversions -> {
                fragment = CurrencyListFragment()
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.flContent, fragment!!).commit()

        item.isChecked = true
        drawerLayout.closeDrawers()

        return true
    }
}