package com.example.currencyconverter.repository

import com.example.currencyconverter.network.retrofitClient

class CurrencyRepository {
    suspend fun getRates(base: String) : Map<String, Double>{
        return try {
            val response = retrofitClient.api.getRates(base)
            response.rates
        }catch (e: Exception){
            android.util.Log.d("!@#", "repo exception!")
            emptyMap<String, Double>()
        }
    }
}