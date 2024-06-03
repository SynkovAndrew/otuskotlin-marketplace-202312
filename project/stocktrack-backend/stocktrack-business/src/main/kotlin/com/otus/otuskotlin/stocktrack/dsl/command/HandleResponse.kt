package com.otus.otuskotlin.stocktrack.dsl.command

import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.stock.ErrorStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.ErrorStocksRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkStocksRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkWithErrorsStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.StocksRepositoryResponse

internal fun SingleStockResponseContext.handleResponse(
    response: StockRepositoryResponse
): SingleStockResponseContext {
    return when (response) {
        is OkStockRepositoryResponse -> copy(response = response.data).finish()
        is ErrorStockRepositoryResponse -> fail(response.errors)
        is OkWithErrorsStockRepositoryResponse -> copy(response = response.data)
            .fail(response.errors)
    }
}

internal fun SearchStocksResponseContext.handleResponse(
    response: StocksRepositoryResponse
): SearchStocksResponseContext {
    return when (response) {
        is OkStocksRepositoryResponse -> copy(response = response.data).finish()
        is ErrorStocksRepositoryResponse -> fail(response.errors)
    }
}