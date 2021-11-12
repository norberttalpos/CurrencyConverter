package hu.bme.aut.currencyconverter.network

import hu.bme.aut.currencyconverter.network.response.CurrencyConversionResponse
import hu.bme.aut.currencyconverter.network.response.CurrencyResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private val retrofit: Retrofit
    private val currencyApi: CurrencyApi

    private const val baseUrl = "https://api.frankfurter.app"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        currencyApi = retrofit.create(CurrencyApi::class.java)
    }

    fun getCurrencies(from: String?, to: String?): Call<CurrencyResponse?>? {
        return currencyApi.getCurrencies(from, to)
    }

    fun getConversion(from: String?, to: String?, amount: Double?): Call<CurrencyConversionResponse?>? {
        return currencyApi.getConversion(from, to, amount)
    }
}