package hu.bme.aut.currencyconverter.network.response

data class CurrencyConversionResponse(
    val from: String,
    val to: String,
    val rates: Map<String, Double>,
)
