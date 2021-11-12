package hu.bme.aut.currencyconverter.data

object ListToQueryStringConverter {
    fun convertListToQueryString(currencyList: List<String?>): String {
        var queryString = ""

        currencyList.forEach { queryString += it }

        return queryString
    }
}