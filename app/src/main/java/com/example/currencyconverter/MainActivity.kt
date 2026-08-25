package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.currencyconverter.model.ConverterUiState
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import com.example.currencyconverter.model.Currency
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverter.navigation.InfoScreen
import com.example.currencyconverter.navigation.MainScreen
import com.example.currencyconverter.navigation.SettingsScreen
import com.example.currencyconverter.view.Info
import com.example.currencyconverter.view.MainScreen
import com.example.currencyconverter.view.MainScreenStarter
import com.example.currencyconverter.view.Settings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyConverterTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = MainScreen
                ) {
                    composable<MainScreen> {
                        MainScreenStarter(navController = navController)
                    }
                    composable<SettingsScreen> {
                        Settings(navController = navController)
                    }
                    composable<InfoScreen> {
                        Info(navController = navController)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_8", showSystemUi = true)
@Composable
fun Preview() {
    CurrencyConverterTheme {
        MainScreen(rememberNavController(),
            ConverterUiState(
                toCurrency = Currency("CAD", "CAD"),
                amount = "100",
                result = "142.23",
                isLoading = true
            ),
            {}, {}, {}, {}, {})
    }
}