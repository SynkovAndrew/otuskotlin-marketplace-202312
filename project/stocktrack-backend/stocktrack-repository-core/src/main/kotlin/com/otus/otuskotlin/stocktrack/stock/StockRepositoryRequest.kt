package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.model.Stock

data class StockRepositoryRequest(
    val stock: Stock
)

data class StockIdRepositoryRequest(
    val stockId: Stock.Id
)

data class StockFilterRepositoryRequest(
    val name: String,
    val category: Stock.Category
)