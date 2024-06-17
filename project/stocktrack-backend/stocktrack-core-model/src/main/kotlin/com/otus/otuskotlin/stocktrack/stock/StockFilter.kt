package com.otus.otuskotlin.stocktrack.stock

data class StockFilter(
    val searchString: String? = null,
    val category: Stock.Category = Stock.Category.NONE
)