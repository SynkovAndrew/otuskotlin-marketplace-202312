package com.otus.otuskotlin.stocktrack.snapshot

import com.otus.otuskotlin.stocktrack.model.ErrorDescription

abstract class BaseStockSnapshotRepository : StockSnapshotRepository {

    protected suspend fun tryReturningOne(
        func: suspend () -> StockSnapshotRepositoryResponse
    ): StockSnapshotRepositoryResponse {
        return try {
            func.invoke()
        } catch (throwable: Throwable) {
            ErrorStockSnapshotRepositoryResponse(
                errorDescription = ErrorDescription(
                    message = throwable.message ?: "",
                    throwable = throwable
                )
            )
        }
    }

    protected suspend fun tryReturningMultiple(
        func: suspend () -> StockSnapshotsRepositoryResponse
    ): StockSnapshotsRepositoryResponse {
        return try {
            func.invoke()
        } catch (throwable: Throwable) {
            ErrorStockSnapshotsRepositoryResponse(
                errorDescription = ErrorDescription(
                    message = throwable.message ?: "",
                    throwable = throwable
                )
            )
        }
    }

    protected fun notSingleResultErrorResponse(id: StockSnapshot.Id): ErrorStockSnapshotRepositoryResponse {
        return ErrorStockSnapshotRepositoryResponse(
            errorDescription = ErrorDescription(
                code = "database-result-error",
                message = "Failed to perform operation " +
                        "on StockSnapshot(id=${id.value}) cause returned result not single"
            )
        )
    }
}