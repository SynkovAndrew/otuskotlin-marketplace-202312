package com.otus.otuskotlin.stocktrack.model

@JvmInline
value class StockLock(private val value: String) {
    fun asString(): String = value

    companion object {
        val NONE = StockLock("")
    }
}
