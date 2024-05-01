package com.otus.otuskotlin.stocktrack.configuration

import com.otus.otuskotlin.stocktrack.ApplicationSettings
import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.SearchStocksResponseProcessor
import com.otus.otuskotlin.stocktrack.SingleStockResponseProcessor

data class KtorApplicationSettings(
    override val coreSettings: CoreSettings,
    override val singleStockResponseProcessor: SingleStockResponseProcessor,
    override val searchStocksResponseProcessor: SearchStocksResponseProcessor
): ApplicationSettings