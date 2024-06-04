package com.otus.otuskotlin.stocktrack.model

import com.otus.otuskotlin.stocktrack.stock.Stock

data class StockFilter(
    val searchString: String? = null,
    val category: Stock.Category? = null
)