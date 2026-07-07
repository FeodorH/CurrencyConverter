CurrencyConverter — Android app for currency conversion
A simple and reliable currency converter with up-to-date exchange rates, written in Kotlin using Jetpack Compose and MVVM architecture.

![Architecture scheme](/images/CurrencyConverterScheme.png)

🚀 Key features
Conversion between any supported currencies (160+ currencies)

Up-to-date exchange rates from the Frankfurter API (updated every 30 minutes)

Cache exchange rates to save data and provide offline access (outdated data)

The currency list is loaded from the API and is always up-to-date.
Loading indicator and network error handling

🏗️ Architecture and technologies
Stack
Language: Kotlin

UI: Jetpack Compose (Material 3)

Asynchronous: Coroutines + Flow

Network: Retrofit + Gson

Architecture: MVVM (ViewModel + StateFlow)

DI: manual injection via constructor

Key architectural solutions
Single base currency (USD) - all rates are requested relative to USD, then the cross-rate is calculated. This guarantees stability and avoids problems with unstable API responses for rare currencies.

Caching of rates and currency list - reduces the number of network requests and ensures operation with poor internet.

Immutable UI State - all screen state is stored in the data class ConverterUiState, updated via copy().

🧩 Project structure
text
app/src/main/java/.../currencyconverter/

├── MainActivity.kt           # Entry point, Compose UI

├── MainViewModel.kt          # Business logic, state

├── model/Currency.kt         # Data models (Currency, ConverterUiState)

├── network/CurrencyApi.kt    # Retrofit interface, DTO

├── repository/CurrencyRepository.kt  # Data handling (cache + network)

└── ui/theme/                 # App theme
🔄 Data flow (MVVM)
User enters amount, selects currencies → UI calls ViewModel methods

ViewModel updates ConverterUiState via update

When "Convert" button is clicked, coroutine is started

Repository returns cached (or fresh) rates relative to USD

ViewModel calculates result via cross-rate and updates state

UI is automatically redrawn

📦 Dependencies
Retrofit 2.11.0

Coroutines 1.7.3

Lifecycle ViewModel Compose 2.8.7

Compose BOM 2024.11.00

📄 License
Educational project. Free to use and modify.

The idea of the project is to show an architecturally clean approach to developing Android applications with an up-to-date technology stack.
