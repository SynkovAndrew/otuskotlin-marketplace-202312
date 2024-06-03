package com.otus.otuskotlin.stocktrack.snapshot

import com.otus.otuskotlin.stocktrack.Repository
import com.otus.otuskotlin.stocktrack.model.ErrorDescription

sealed interface StockSnapshotsRepositoryResponse : Repository.Response<List<StockSnapshot>>

data class OkStockSnapshotsRepositoryResponse(
    val data: List<StockSnapshot>
) : StockSnapshotsRepositoryResponse

data class ErrorStockSnapshotsRepositoryResponse(
    val errors: List<ErrorDescription>
) : StockSnapshotsRepositoryResponse {
    constructor(errorDescription: ErrorDescription): this(listOf(errorDescription))
}