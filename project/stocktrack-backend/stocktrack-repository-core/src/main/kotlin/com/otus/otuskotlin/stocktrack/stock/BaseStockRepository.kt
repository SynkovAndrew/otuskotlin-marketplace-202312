package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.Stock

abstract class BaseStockRepository : StockRepository {

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
}