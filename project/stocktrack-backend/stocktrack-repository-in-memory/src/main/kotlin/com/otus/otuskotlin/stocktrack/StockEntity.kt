package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.stock.StockLock

data class StockEntity(
    val id: String,
    val name: String,
    val category: String,
    val lock: String?,
)

object StockEntityMapper {

    fun toEntity(stock: Stock): StockEntity {
        return StockEntity(
            id = stock.id.takeIf { it != Stock.Id.NONE }?.value ?: error("id is empty"),
            name = stock.name.takeIf { it.isNotEmpty() } ?: error("name is empty"),
            category = stock.category.takeIf { it != Stock.Category.NONE }?.name ?: error("category is empty"),
            lock = stock.lock.takeIf { it != StockLock.NONE }?.value
        )
    }

    fun fromEntity(stock: StockEntity): Stock {
        return Stock(
            id = Stock.Id(value = stock.id),
            name = stock.name,
            category = Stock.Category.valueOf(stock.category),
            lock = stock.lock?.let { StockLock(it) } ?: StockLock.NONE
        )
    }
}