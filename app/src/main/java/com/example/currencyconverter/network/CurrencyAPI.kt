package com.example.currencyconverter.network

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// DTO - object for json data
data class ExchangeRatesDto(
    val base: String,          // "USD"
    val date: String,          // "2024-06-06"
    val rates: Map<String, Double> // {"RUB": 89.5, "EUR": 0.92}
)

interface CurrencyAPI {
    @GET("latest")
    suspend fun getRates(
        @Query("base") baseCurrency: String
    ): ExchangeRatesDto

    @GET("currencies")
    suspend fun getCurrencies(): Map<String, String>
}

object retrofitClient{
    private const val BASE_URL = "https://api.frankfurter.app/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: CurrencyAPI by lazy {
        retrofit.create(CurrencyAPI::class.java)
    }
}

