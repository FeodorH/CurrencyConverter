package com.example.currencyconverter.model

data class Currency(
    val code: String,
    val name: String,
)

data class ConverterUiState(
    val amount: String = "",
    val fromCurrency: Currency = Currency("USD", "USD"),
    val toCurrency: Currency = Currency("RUB", "RUB"),
    val result: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val availableCurrencies: List<Currency> = listOf(
        Currency("USD", "USD"), Currency("EUR", "EUR"),
        Currency("RUB", "RUB"), Currency("GBP", "GBP")
    )
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