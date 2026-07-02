package com.example.currencyconverter.cache

import com.example.currencyconverter.model.CachedCurrencies
import com.example.currencyconverter.model.CachedRates
import com.example.currencyconverter.model.Currency
import com.example.currencyconverter.repository.CacheOfCurrencies

class CurrencyCache : CacheOfCurrencies {
    override var cachedCurrencies: CachedCurrencies? = null
    override var cachedRatesUSD: CachedRates? = null
    override val CACHE_DURATION_CURRENCIES = 24 * 60 * 60 * 1000L  // 24 hours
    override val CACHE_DURATION_RATES = 30 * 60 * 1000L //30 minutes
}