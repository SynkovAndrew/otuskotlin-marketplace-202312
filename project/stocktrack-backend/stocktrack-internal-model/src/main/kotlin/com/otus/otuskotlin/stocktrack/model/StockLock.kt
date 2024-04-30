package com.otus.otuskotlin.stocktrack.model

@JvmInline
value class StockLock(val value: String) {

    companion object {
        val NONE = StockLock("")
    }
}
