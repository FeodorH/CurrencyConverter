package com.example.currencyconverter.repository

import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.network.retrofitClient
import kotlin.math.abs

class CurrencyRepository {
    private data class CachedCurrencies(
        val data: List<Currency>,
        val timestamp: Long
    )

    private data class CachedRate(
        val data:  Map<String, Double>,
        val timestamp: Long
    )

    private var cachedCurrencies: CachedCurrencies? = null
    private val cachedRates = mutableMapOf<String, CachedRate>()
    private val CACHE_DURATION_CURRENCIES = 24 * 60 * 60 * 1000L  // 24 hours
    private val CACHE_DURATION_RATES = 30 * 60 * 1000L //30 minutes

    suspend fun getRates(base: String) : Map<String, Double>{
        val timeNow = System.currentTimeMillis()
        val cache = cachedRates[base]
        if(cache != null||(timeNow - cachedRates[base]!!.timestamp) > CACHE_DURATION_RATES){
            val result = try {
                val response = retrofitClient.api.getRates(base)
                response.rates
            }catch (e: Exception){
                android.util.Log.d("!@#", "repo get rates exception!")
                cache?.data?: emptyMap<String, Double>()
            }
            val newCache = CachedRate(result, timeNow)
            cachedRates.put(base,newCache)
            return result
        }else{
            return cache!!.data
        }
    }

    suspend fun getCurrencies(): List<Currency>{
        val timeNow = System.currentTimeMillis()
        if(cachedCurrencies != null && (timeNow - cachedCurrencies!!.timestamp) > CACHE_DURATION_CURRENCIES){
            return cachedCurrencies!!.data
        }else{
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