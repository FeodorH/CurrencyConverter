package com.example.currencyconverter.repository

import com.example.currencyconverter.view_model.RepositoryDI
import com.example.currencyconverter.cache.CurrencyCache
import com.example.currencyconverter.model.CachedCurrencies
import com.example.currencyconverter.model.CachedRates
import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.model.ExchangeRatesDto
import com.example.currencyconverter.network.retrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyRepository(
    val cache: CacheOfCurrencies = CurrencyCache(),
    val networkOfCurrencies: NetworkOfCurrencies = retrofitClient
) : RepositoryDI {

    override suspend fun getRates(base: String) : Map<String, Double> = withContext(Dispatchers.IO) {
        val timeNow = System.currentTimeMillis()
        val cached = cache.cachedRatesUSD

        if (cached != null && (timeNow - cached.timestamp) < cache.CACHE_DURATION_RATES) {
            cached.data
        } else {
            try {
                val response = networkOfCurrencies.getRates("USD")
                val rates = response.rates
                cache.cachedRatesUSD = CachedRates(rates, timeNow)
                rates
            } catch (e: Exception) {
                android.util.Log.d("!@#", "repo get rates exception!")
                cached?.data ?: emptyMap()
            }
        }

    }

    override suspend fun getCurrencies(): List<Currency> = withContext(Dispatchers.IO){
        val timeNow = System.currentTimeMillis()
        val cached = cache.cachedCurrencies
        if(cached != null && (timeNow - cached.timestamp) < cache.CACHE_DURATION_CURRENCIES){
            cached.data
        } else{
            val result = try {
                val currenciesMap  = networkOfCurrencies.getCurrencies()

                currenciesMap.map { (code, name) ->
                    Currency(code = code, name = name)
                }.sortedBy { it.code }
            }catch (e: Exception){
                android.util.Log.d("!@#", "repo get curr-s exception!")
                emptyList<Currency>()
            }
            cache.cachedCurrencies = CachedCurrencies(result, timeNow)
            result
        }
    }
}

interface CacheOfCurrencies{
    var cachedRatesUSD: CachedRates?
    var cachedCurrencies: CachedCurrencies?
    val CACHE_DURATION_RATES: Long
    val CACHE_DURATION_CURRENCIES: Long
}

interface NetworkOfCurrencies{
    suspend fun getCurrencies(): Map<String, String>
    suspend fun getRates(base: String): ExchangeRatesDto
}