package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.currencyconverter.model.ConverterUiState
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.currencyconverter.model.Currency
import androidx.lifecycle.compose.collectAsStateWithLifecycle


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyConverterTheme {
                ViewStarter()
            }
        }
    }
}

@Composable
fun ViewStarter(){
    val viewModel : MainViewModel = viewModel()
    val uiState : ConverterUiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(state = uiState,
        onFromCurrencyChange = viewModel::updateFromCurrency,
        onAmountChange = viewModel::updateAmount,
        onToCurrencyChange = viewModel::updateToCurrency,
        onResultChange = viewModel::updateResult,
        onClickConvertButton = viewModel::convertButtonClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(state: ConverterUiState,
               onFromCurrencyChange:(Currency)-> Unit,
               onAmountChange:(String)-> Unit,
               onToCurrencyChange: (Currency) -> Unit,
               onResultChange: (String) -> Unit,
               onClickConvertButton: () -> Unit
) {
    var fromExpanded by remember { mutableStateOf(false) }
    var toExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Currency from:",
                style = MaterialTheme.typography.labelMedium
            )

            ExposedDropdownMenuBox(
                expanded = fromExpanded,
                onExpandedChange = { fromExpanded = !fromExpanded }
            ) {
                OutlinedTextField(
                    value = state.fromCurrency?.code ?: "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fromExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable) // depends dropdownMenu and text
                )

                ExposedDropdownMenu(
                    expanded = fromExpanded,
                    onDismissRequest = { fromExpanded = false }
                ) {
                    state.availableCurrencies.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text("${currency.code} - ${currency.name}") },
                            onClick = {
                                onFromCurrencyChange(currency)
                                fromExpanded = false
                            }
                        )
                    }
                }
            }

            Text(
                text = "Amount:",
                style = MaterialTheme.typography.labelMedium
            )
            OutlinedTextField(
                value = state.amount,
                onValueChange = onAmountChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Currency to:",
                style = MaterialTheme.typography.labelMedium
            )

            ExposedDropdownMenuBox(
                expanded = toExpanded,
                onExpandedChange = { toExpanded = !toExpanded }
            ) {
                OutlinedTextField(
                    value = state.toCurrency?.code ?: "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = toExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = toExpanded,
                    onDismissRequest = { toExpanded = false }
                ) {
                    state.availableCurrencies.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text("${currency.code} - ${currency.name}") },
                            onClick = {
                                onToCurrencyChange(currency)
                                toExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Result:",
                style = MaterialTheme.typography.labelMedium
            )
            OutlinedTextField(
                value = state.result,
                onValueChange = onResultChange,
                readOnly = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onClickConvertButton() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Convert")
            }

            if(state.isLoading){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            //Error
            if (state.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true, device = "id:pixel_8", showSystemUi = true)
@Composable
fun Preview() {
    CurrencyConverterTheme {
        MainScreen(ConverterUiState(toCurrency = Currency("CAD","CAD"),amount = "100",result = "142.23", isLoading = false),
            {},{},{},{},{})
    }
}