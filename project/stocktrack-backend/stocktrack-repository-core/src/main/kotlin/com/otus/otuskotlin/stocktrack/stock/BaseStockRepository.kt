package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.model.ErrorDescription

abstract class BaseStockRepository : EnrichableStockRepository {

    protected suspend fun tryReturningOne(func: suspend () -> StockRepositoryResponse): StockRepositoryResponse {
        return try {
            func.invoke()
        } catch (throwable: Throwable) {
            ErrorStockRepositoryResponse(
                errorDescription = ErrorDescription(
                    message = throwable.message ?: "",
                    throwable = throwable
                )
            )
        }
    }

    protected suspend fun tryReturningMultiple(func: suspend () -> StocksRepositoryResponse): StocksRepositoryResponse {
        return try {
            func.invoke()
        } catch (throwable: Throwable) {
            ErrorStocksRepositoryResponse(
                errorDescription = ErrorDescription(
                    message = throwable.message ?: "",
                    throwable = throwable
                )
            )
        }
    }

    protected fun stockNotFoundErrorResponse(stockId: Stock.Id): ErrorStockRepositoryResponse {
        return ErrorStockRepositoryResponse(
            errorDescription = ErrorDescription(
                code = "stock-not-found",
                message = "Stock(id=${stockId.value}) is not found"
            )
        )
    }

    protected fun emptyRequestLockErrorResponse(stockId: Stock.Id): ErrorStockRepositoryResponse {
        return ErrorStockRepositoryResponse(
            errorDescription = ErrorDescription(
                code = "empty-request-lock",
                message = "Failed to perform write operation on Stock(id=${stockId.value}) with empty lock in request"
            )
        )
    }

    protected fun emptyStockLockErrorResponse(stockId: Stock.Id): ErrorStockRepositoryResponse {
        return ErrorStockRepositoryResponse(
            errorDescription = ErrorDescription(
                code = "empty-stock-lock",
                message = "Failed to perform write operation on Stock(id=${stockId.value}) with empty lock"
            )
        )
    }

    protected fun concurrencyErrorResponse(stockId: Stock.Id): ErrorStockRepositoryResponse {
        return ErrorStockRepositoryResponse(
            errorDescription = ErrorDescription(
                code = "concurrency-error",
                message = "Failed to perform write operation on Stock(id=${stockId.value}) with lock updated already"
            )
        )
    }

    protected fun notSingleResultErrorResponse(stockId: Stock.Id): ErrorStockRepositoryResponse {
        return ErrorStockRepositoryResponse(
            errorDescription = ErrorDescription(
                code = "database-result-error",
                message = "Failed to perform operation on Stock(id=${stockId.value}) cause returned result not single"
            )
        )
    }
}