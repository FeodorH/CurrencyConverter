package com.example.currencyconverter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.model.ConverterUiState
import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ConverterUiState())//inner
    val uiState: StateFlow<ConverterUiState> = _uiState.asStateFlow()//outer
    val repository: CurrencyRepository = CurrencyRepository()

    init{
        loadCurrencies()
    }

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

    fun convertButtonClick(){
        viewModelScope.launch {
            val amount : Double = _uiState.value.amount.toDoubleOrNull() ?: 0.0
            val fromCurrency = _uiState.value.fromCurrency
            val toCurrency = _uiState.value.toCurrency

            if(fromCurrency == null || toCurrency == null){
                _uiState.update { it.copy(error = "Set all currencies") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }
            android.util.Log.d("!@#", "loading...")

            val course = repository.getRates(fromCurrency.code)
            android.util.Log.d("!@#", "repo course get")

            if(!course.isEmpty()){
                val rate = course[toCurrency.code]
                android.util.Log.d("!@#", "rate get")
                if(rate != null){
                    _uiState.update { it.copy(result = (amount*rate).toString(),
                        isLoading = false)}
                }else{
                    _uiState.update {
                        it.copy(
                            error = "Not found course for ${toCurrency.code}",
                            isLoading = false
                        )
                    }
                }
            }else{
                android.util.Log.d("!@#", "course else")
                _uiState.update {
                    it.copy(
                        error = "Bad internet-connection",
                        isLoading = false
                    )
                }
            }
        }
    }
}