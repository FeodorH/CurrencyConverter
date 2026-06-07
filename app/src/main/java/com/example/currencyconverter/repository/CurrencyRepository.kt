package com.example.currencyconverter.repository

import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.network.retrofitClient
import kotlin.math.abs

class CurrencyRepository {
    private data class CachedCurrencies(
        val list: List<Currency>,
        val timestamp: Long
    )

    private var cachedCurrencies: CachedCurrencies? = null
    private val CACHE_DURATION_MS = 30 * 60 * 1000L  // 30 minutes

    suspend fun getRates(base: String) : Map<String, Double>{
        return try {
            val response = retrofitClient.api.getRates(base)
            response.rates
        }catch (e: Exception){
            android.util.Log.d("!@#", "repo get rates exception!")
            emptyMap<String, Double>()
        }
    }

    suspend fun getCurrencies(): List<Currency>{
        if(cachedCurrencies != null && abs(System.currentTimeMillis() - cachedCurrencies!!.timestamp) <CACHE_DURATION_MS){
            return cachedCurrencies!!.list
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
            cachedCurrencies = CachedCurrencies(result, System.currentTimeMillis())
            return result
        }
    }
}