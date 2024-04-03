package com.otus.otuskotlin.stocktrack.model

data class StockFilter(
    val searchString: String? = null,
    val category: Stock.Category? = null
)