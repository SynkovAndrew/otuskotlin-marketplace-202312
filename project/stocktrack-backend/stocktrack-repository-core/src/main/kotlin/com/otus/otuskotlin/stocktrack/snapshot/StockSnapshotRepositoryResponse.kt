package com.otus.otuskotlin.stocktrack.snapshot

import com.otus.otuskotlin.stocktrack.Repository
import com.otus.otuskotlin.stocktrack.model.ErrorDescription

sealed interface StockSnapshotRepositoryResponse : Repository.Response<StockSnapshot>

data class OkStockSnapshotRepositoryResponse(
    val data: StockSnapshot
) : StockSnapshotRepositoryResponse

data class ErrorStockSnapshotRepositoryResponse(
    val errors: List<ErrorDescription>
) : StockSnapshotRepositoryResponse {
    constructor(errorDescription: ErrorDescription): this(listOf(errorDescription))
}