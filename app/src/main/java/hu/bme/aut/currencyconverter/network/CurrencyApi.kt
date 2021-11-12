package hu.bme.aut.currencyconverter.network

import hu.bme.aut.currencyconverter.network.response.CurrencyConversionResponse
import hu.bme.aut.currencyconverter.network.response.CurrencyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("/latest")
    fun getCurrencies(
        @Query("from") from: String?,
        @Query("to") to: String?,
    ): Call<CurrencyResponse?>?

    @GET("/latest")
    fun getConversion(
        @Query("from") from: String?,
        @Query("to") to: String?,
        @Query("amount") amount: Double?
    ): Call<CurrencyConversionResponse?>?
}