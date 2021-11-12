package hu.bme.aut.currencyconverter.view.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.currencyconverter.databinding.ActivityMainBinding

class CurrencyListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}