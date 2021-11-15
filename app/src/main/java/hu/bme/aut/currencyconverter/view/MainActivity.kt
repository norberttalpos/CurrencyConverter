package hu.bme.aut.currencyconverter.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.currencyconverter.R
import hu.bme.aut.currencyconverter.databinding.ActivityMainBinding
import hu.bme.aut.currencyconverter.view.list.CurrencyListFragment
import hu.bme.aut.currencyconverter.view.select.SelectListFragment


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        this.initDrawer()

        supportFragmentManager.beginTransaction().replace(R.id.flContent, CurrencyListFragment()).commit()

        setContentView(binding.root)
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
                fragment = CurrencyListFragment()
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