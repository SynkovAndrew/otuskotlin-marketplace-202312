package com.otus.otuskotlin.stocktrack.model

import java.util.UUID

data class Stock(
    val id: Id = Id(value = UUID.randomUUID().toString()),
    val name: String = "",
    val category: Category = Category.NONE,
    val lock: StockLock = StockLock.NONE,
    val permissions: Set<StockPermission> = emptySet()
) {

    enum class Category {
        SHARE, BOND, NONE
    }

    @JvmInline
    value class Id(val value: String)

    companion object {
        val NONE = Stock()
    }
}