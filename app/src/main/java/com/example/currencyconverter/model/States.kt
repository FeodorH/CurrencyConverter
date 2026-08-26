package com.example.currencyconverter.model

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

data class SettingsUiState(
    val isCacheUsed : Boolean = true,
    val currencyCacheDuration: Long = 24 * 60 * 60 * 1000L,
    val ratesCacheDuration: Long = 30 * 60 * 1000L,
    val baseCurrency: String = "USD"
)