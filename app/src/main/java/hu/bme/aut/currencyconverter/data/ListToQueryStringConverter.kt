package hu.bme.aut.currencyconverter.data

import hu.bme.aut.currencyconverter.data.repository.selection.CurrencyName

object ListToQueryStringConverter {
    fun convertListToQueryString(currencyList: List<CurrencyName>): String {
        var queryString = ""

        currencyList.forEachIndexed { index, element ->
            queryString += element.name
            if(index != currencyList.size - 1)
                queryString += ","
        }

        return queryString
    }
}