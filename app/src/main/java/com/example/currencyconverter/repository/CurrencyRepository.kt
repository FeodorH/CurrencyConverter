package com.example.currencyconverter.repository

import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.network.retrofitClient
import kotlin.math.abs

class CurrencyRepository {
    private data class CachedCurrencies(
        val data: List<Currency>,
        val timestamp: Long
    )

    private data class CachedRates(// курсы относительно USD
        val data: Map<String, Double>,
        val timestamp: Long
    )

    private var cachedCurrencies: CachedCurrencies? = null
    private var cachedRatesUSD: CachedRates? = null
    private val CACHE_DURATION_CURRENCIES = 24 * 60 * 60 * 1000L  // 24 hours
    private val CACHE_DURATION_RATES = 30 * 60 * 1000L //30 minutes

    suspend fun getRates(base: String) : Map<String, Double>{
        val timeNow = System.currentTimeMillis()

        val cached = cachedRatesUSD
        if (cached != null && (timeNow - cached.timestamp) < CACHE_DURATION_RATES) {
            return cached.data
        } else {
            return try {
                val response = retrofitClient.api.getRates("USD")
                val rates = response.rates
                cachedRatesUSD = CachedRates(rates, timeNow)
                rates
            } catch (e: Exception) {
                android.util.Log.d("!@#", "repo get rates exception!")
                cached?.data ?: emptyMap()
            }
        }
    }

    suspend fun getCurrencies(): List<Currency>{
        val timeNow = System.currentTimeMillis()
        val cached = cachedCurrencies
        if(cached != null && (timeNow - cached.timestamp) < CACHE_DURATION_CURRENCIES){
            return cached.data
        } else{
            val result = try {
                val currenciesMap = retrofitClient.api.getCurrencies()

                currenciesMap.map { (code, name) ->
                    Currency(code = code, name = name)
                }.sortedBy { it.code }
            }catch (e: Exception){
                android.util.Log.d("!@#", "repo get curr-s exception!")
                emptyList<Currency>()
            }
            cachedCurrencies = CachedCurrencies(result, timeNow)
            return result
        }
    }
}