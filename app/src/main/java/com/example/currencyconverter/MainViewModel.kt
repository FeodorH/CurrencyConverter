package com.example.currencyconverter

import androidx.lifecycle.ViewModel
import com.example.currencyconverter.model.ConverterUiState
import com.example.currencyconverter.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ConverterUiState())//inner
    val uiState: StateFlow<ConverterUiState> = _uiState.asStateFlow()//outer

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
        val course : Double = 2.0
        var result : Double = course*(_uiState.value.amount.toDoubleOrNull()?:0.0)
        updateResult(result.toString())
    }
}