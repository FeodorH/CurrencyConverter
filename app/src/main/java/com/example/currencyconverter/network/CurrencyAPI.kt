package com.example.currencyconverter.network

import com.example.currencyconverter.model.ExchangeRatesDto
import com.example.currencyconverter.repository.NetworkOfCurrencies
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {
    @GET("latest")
    suspend fun getRates(
        @Query("base") baseCurrency: String
    ): ExchangeRatesDto

    @GET("currencies")
    suspend fun getCurrencies(): Map<String, String>
}

object retrofitClient : NetworkOfCurrencies{
    private const val BASE_URL = "https://api.frankfurter.app/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val api: CurrencyAPI by lazy {
        retrofit.create(CurrencyAPI::class.java)
    }

    override suspend fun getRates(base : String) : ExchangeRatesDto = api.getRates(base)

    override suspend fun getCurrencies() : Map<String, String> = api.getCurrencies()
}

