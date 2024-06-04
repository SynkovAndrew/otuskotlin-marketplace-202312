package com.otus.otuskotlin.stocktrack.snapshot

import com.otus.otuskotlin.stocktrack.stock.Stock
import kotlinx.datetime.Instant
import java.math.BigDecimal

data class StockSnapshot(
    val id: Id,
    val stockId: Stock.Id,
    val value: BigDecimal,
    val timestamp: Instant
) {


    @JvmInline
    value class Id(val value: String)  {
        companion object {
            val NONE = Id("")
        }
    }
}