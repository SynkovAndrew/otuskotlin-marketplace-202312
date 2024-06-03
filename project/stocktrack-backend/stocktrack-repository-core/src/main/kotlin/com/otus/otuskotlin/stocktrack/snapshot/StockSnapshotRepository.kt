package com.otus.otuskotlin.stocktrack.snapshot

import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.Stock

interface StockSnapshotRepository {

    suspend fun create(stockSnapshot: StockSnapshot): StockSnapshotRepositoryResponse

    suspend fun findByStockId(id: Stock.Id): StockSnapshotsRepositoryResponse

}