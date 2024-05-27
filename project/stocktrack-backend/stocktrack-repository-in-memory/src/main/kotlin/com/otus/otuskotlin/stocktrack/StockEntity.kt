package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.model.Stock

data class StockEntity(
    val id: String,
    val name: String,
    val category: String,
    val lock: String?,
)

object StockEntityMapper {

    fun toEntity(stock: Stock): StockEntity
}