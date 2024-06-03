package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.model.StockLock

data class StockRepositoryRequest(
    val stock: Stock
)

data class StockIdRepositoryRequest(
    val stockId: Stock.Id,
    val lock: StockLock = StockLock.NONE
) {
    fun isLockNone(): Boolean {
        return lock == StockLock.NONE
    }
}

data class StockFilterRepositoryRequest(
    val name: String?,
    val category: Stock.Category?
)