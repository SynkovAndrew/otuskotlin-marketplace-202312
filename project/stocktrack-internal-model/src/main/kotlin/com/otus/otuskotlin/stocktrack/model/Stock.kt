package com.otus.otuskotlin.stocktrack.model

import java.util.UUID

data class Stock(
    val id: StockId = StockId(value = UUID.randomUUID().toString()),
    val name: String = "",
    val category: Category = Category.NONE,
    val lock: StockLock = StockLock.NONE,
    val permissions: Set<StockPermission> = emptySet()
) {

    enum class Category {
        SHARE, BOND, NONE
    }

    companion object {
        val NONE = Stock()
    }
}

@JvmInline
value class StockId(private val value: String) {
    fun asString(): String = value
}
