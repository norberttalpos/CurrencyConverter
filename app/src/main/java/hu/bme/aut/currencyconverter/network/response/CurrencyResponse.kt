package hu.bme.aut.currencyconverter.network.response

import java.util.*

data class CurrencyResponse(
    val base: String?,
    val date: Date?,
    val rates: List<Currency>?
)