package com.example.currencyconverter.model

data class Currency(
    val code: String,
    val name: String,
)

data class CachedCurrencies(
    val data: List<Currency>,
    val timestamp: Long
)

data class CachedRates(// rates respecting USD
    val data: Map<String, Double>,
    val timestamp: Long
)

data class ExchangeRatesDto(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)