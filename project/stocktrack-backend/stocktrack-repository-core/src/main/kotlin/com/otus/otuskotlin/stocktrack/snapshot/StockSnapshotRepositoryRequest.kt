package com.otus.otuskotlin.stocktrack.snapshot

import com.otus.otuskotlin.stocktrack.model.Stock

data class StockSnapshotsRepositoryRequest(
    val snapshot: List<StockSnapshot>
)

data class StockSnapshotsStockIdRepositoryRequest(
    val stockId: Stock.Id
)