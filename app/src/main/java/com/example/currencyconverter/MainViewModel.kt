package com.example.currencyconverter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.model.ConverterUiState
import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    val repository: RepositoryDI = CurrencyRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConverterUiState())//inner
    val uiState: StateFlow<ConverterUiState> = _uiState.asStateFlow()//outer

    init{
        loadCurrencies()
    }

    //started load currencies
    fun loadCurrencies(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currencyList = repository.getCurrencies()
            if (!currencyList.isEmpty()) {
                val fromCurrency : Currency = currencyList.find { it.code == "USD" } ?: currencyList.first()
                val toCurrency: Currency = currencyList.find { it.code == "RUB" }
                    ?: currencyList.getOrNull(1)
                    ?: fromCurrency
                _uiState.update { it.copy(availableCurrencies = currencyList,
                    fromCurrency = fromCurrency,
                    toCurrency = toCurrency,
                    error = null)}
            }else{
                _uiState.update { it.copy(error = "Not any currencies found") }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun updateFromCurrency(newCurrency: Currency){
        _uiState.update { it.copy(fromCurrency = newCurrency) }
    }

    fun updateToCurrency(newCurrency: Currency){
        _uiState.update { it.copy(toCurrency = newCurrency) }
    }

    fun updateAmount(newAmount: String){
        _uiState.update { it.copy(amount = newAmount) }
    }

    fun updateResult(newResult: String){
        _uiState.update { it.copy(result = newResult) }
    }

    fun convertButtonClick() {
        viewModelScope.launch {
            val state = _uiState.value
            val amount = state.amount.toDoubleOrNull() ?: 0.0
            val fromCurrency = state.fromCurrency
            val toCurrency = state.toCurrency

            if (_uiState.value.isLoading) return@launch//if this coroutine already run

            if (fromCurrency == null || toCurrency == null) {
                _uiState.update { it.copy(error = "Set both currencies") }
                return@launch
            }

            if(fromCurrency == toCurrency){
                _uiState.update { it.copy(result = amount.toString()) }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            // Query to USD(always)
            val usdRates = repository.getRates("USD")

            if (usdRates.isNotEmpty()) {
                val rateFrom = usdRates[fromCurrency.code] ?: 1.0
                val rateTo = usdRates[toCurrency.code] ?: 1.0
                val crossRate = rateTo / rateFrom
                val result = amount * crossRate

                _uiState.update {
                    it.copy(
                        result = result.toString(),
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        error = "Failed to load rates. Check internet.",
                        isLoading = false
                    )
                }
            }
        }
    }
}

interface RepositoryDI{
    suspend fun getRates(base: String) : Map<String, Double>
    suspend fun getCurrencies(): List<Currency>
}