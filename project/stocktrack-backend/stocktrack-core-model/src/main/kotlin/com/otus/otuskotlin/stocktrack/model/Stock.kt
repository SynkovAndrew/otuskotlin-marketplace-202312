package com.otus.otuskotlin.stocktrack.model

data class Stock(
    val id: Id = Id.NONE,
    val name: String = "",
    val category: Category = Category.NONE,
    val lock: StockLock = StockLock.NONE,
    val permissions: Set<StockPermission> = emptySet()
) {

    enum class Category {
        SHARE, BOND, NONE
    }

    @JvmInline
    value class Id(val value: String) {
        companion object {
            val NONE = Id("")
        }
    }

    companion object {
        val NONE = Stock()
    }
}