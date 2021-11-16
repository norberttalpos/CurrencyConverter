package hu.bme.aut.currencyconverter.view

import androidx.annotation.DrawableRes
import hu.bme.aut.currencyconverter.R
import hu.bme.aut.currencyconverter.data.CurrencyEnum

@DrawableRes()
fun getImageResource(name: String): Int {
    return when (name) {
        CurrencyEnum.HUF.name -> R.drawable.huf
        CurrencyEnum.USD.name -> R.drawable.usd
        CurrencyEnum.EUR.name -> R.drawable.eur
        CurrencyEnum.CHF.name -> R.drawable.chf
        CurrencyEnum.GBP.name -> R.drawable.gbp

        CurrencyEnum.JPY.name -> R.drawable.jpy
        CurrencyEnum.AUD.name -> R.drawable.aud
        CurrencyEnum.CAD.name -> R.drawable.cad
        CurrencyEnum.CNY.name -> R.drawable.cny
        CurrencyEnum.HKD.name -> R.drawable.hkd

        CurrencyEnum.NZD.name -> R.drawable.nzd
        CurrencyEnum.SEK.name -> R.drawable.sek
        CurrencyEnum.KRW.name -> R.drawable.krw
        CurrencyEnum.SGD.name -> R.drawable.sgd
        CurrencyEnum.NOK.name -> R.drawable.nok
        else -> R.drawable.empty
    }
}