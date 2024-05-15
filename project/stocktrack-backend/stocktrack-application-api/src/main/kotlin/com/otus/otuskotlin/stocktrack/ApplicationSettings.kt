package com.otus.otuskotlin.stocktrack

interface ApplicationSettings {
    val singleStockResponseProcessor: SingleStockResponseProcessor
    val searchStocksResponseProcessor: SearchStocksResponseProcessor
    val coreSettings: CoreSettings
}
