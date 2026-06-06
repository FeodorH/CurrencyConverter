package com.example.currencyconverter.model

data class Currency(
    val code: String,
    val name: String,
)

//class for json -> object
data class ExchangeRatesDto(
    val base: String,
    val rates: Map<String, Double>
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