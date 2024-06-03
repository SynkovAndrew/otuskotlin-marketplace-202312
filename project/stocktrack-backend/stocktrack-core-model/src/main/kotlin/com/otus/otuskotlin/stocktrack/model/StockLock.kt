package com.otus.otuskotlin.stocktrack.model

@JvmInline
value class StockLock(val value: String) {

    fun isNone(): Boolean {
        return this == NONE
    }

    companion object {
        val NONE = StockLock("")
    }
}
