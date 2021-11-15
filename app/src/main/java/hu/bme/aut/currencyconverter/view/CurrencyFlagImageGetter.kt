package hu.bme.aut.currencyconverter.view

import androidx.annotation.DrawableRes
import hu.bme.aut.currencyconverter.R
import hu.bme.aut.currencyconverter.data.CurrencyEnum

object CurrencyFlagImageGetter {
    @DrawableRes()
    fun getImageResource(name: String): Int {
        return when (name) {
            CurrencyEnum.HUF.name -> R.drawable.huf
            CurrencyEnum.USD.name -> R.drawable.usd
            CurrencyEnum.EUR.name -> R.drawable.eur
            CurrencyEnum.CHF.name -> R.drawable.chf
            CurrencyEnum.GBP.name -> R.drawable.gbp
            else -> R.drawable.empty
        }
    }
}