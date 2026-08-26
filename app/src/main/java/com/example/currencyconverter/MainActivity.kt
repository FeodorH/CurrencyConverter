package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverter.navigation.InfoScreen
import com.example.currencyconverter.navigation.MainScreen
import com.example.currencyconverter.navigation.SettingsScreen
import com.example.currencyconverter.view.Info
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