package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.stock.StockPermission

object StubStockRepository {
    private val stocks = listOf(
        Stock(
            id = Stock.Id(value = "1"),
            name = "Gazprom",
            category = Stock.Category.SHARE,
            permissions = setOf(StockPermission.READ)
        ),
        Stock(
            id = Stock.Id(value = "2"),
            name = "Rosbank",
            category = Stock.Category.BOND,
            permissions = setOf(StockPermission.READ)
        ),
        Stock(
            id = Stock.Id(value = "3"),
            name = "Vk",
            category = Stock.Category.SHARE,
            permissions = setOf(StockPermission.READ)
        )
    )

    fun findAll(): List<Stock> {
        return stocks
    }

    fun findById(stockId: Stock.Id): Stock {
        return stocks.firstOrNull { stockId == it.id }
            ?: throw StockNotFoundException("Stock(id=${stockId.value}) not found")
    }

    class StockNotFoundException(message: String) : Exception(message)
}