package com.otus.otuskotlin.stocktrack.snapshot

import com.otus.otuskotlin.stocktrack.stock.Stock

interface StockSnapshotRepository {

    suspend fun create(stockSnapshot: StockSnapshot): StockSnapshotRepositoryResponse

    suspend fun findByStockId(id: Stock.Id): StockSnapshotsRepositoryResponse

}