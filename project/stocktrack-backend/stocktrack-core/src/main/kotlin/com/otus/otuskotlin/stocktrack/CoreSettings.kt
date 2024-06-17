package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.snapshot.StockSnapshotRepository
import com.otus.otuskotlin.stocktrack.stock.StockRepository

data class CoreSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val prodStockRepository: StockRepository,
    val testStockRepository: StockRepository,
    val stubStockRepository: StockRepository,
    val stockSnapshotRepository: StockSnapshotRepository
)
