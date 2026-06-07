package com.example.currencyconverter.repository

import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.network.retrofitClient

class CurrencyRepository {
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
        return try {
            val currenciesMap = retrofitClient.api.getCurrencies()

            currenciesMap.map { (code, name) ->
                Currency(code = code, name = name)
            }.sortedBy { it.code }
        }catch (e: Exception){
            android.util.Log.d("!@#", "repo get curr-s exception!")
            emptyList<Currency>()
        }
    }
}