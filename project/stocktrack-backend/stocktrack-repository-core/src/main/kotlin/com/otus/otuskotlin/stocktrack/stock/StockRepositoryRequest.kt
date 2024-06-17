package com.otus.otuskotlin.stocktrack.stock

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
    val name: String? = null,
    val category: Stock.Category = Stock.Category.NONE
)