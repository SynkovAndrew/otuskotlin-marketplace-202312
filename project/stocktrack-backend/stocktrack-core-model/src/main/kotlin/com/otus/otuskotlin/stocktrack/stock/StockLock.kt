package com.otus.otuskotlin.stocktrack.stock

@JvmInline
value class StockLock(val value: String) {

    fun isNone(): Boolean {
        return this == NONE
    }

    companion object {
        val NONE = StockLock("")
    }
}
