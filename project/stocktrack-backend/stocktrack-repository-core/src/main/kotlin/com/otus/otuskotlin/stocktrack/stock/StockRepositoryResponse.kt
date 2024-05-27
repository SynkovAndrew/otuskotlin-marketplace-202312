package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.Repository
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.Stock

sealed interface StockRepositoryResponse : Repository.Response<Stock>

data class OkStockRepositoryResponse(
    val data: Stock
) : StockRepositoryResponse

data class ErrorStockRepositoryResponse(
    val errors: List<ErrorDescription>
) : StockRepositoryResponse {
    constructor(errorDescription: ErrorDescription): this(listOf(errorDescription))
}

data class OkWithErrorsStockRepositoryResponse(
    val data: Stock,
    val errors: List<ErrorDescription> = emptyList()
) : StockRepositoryResponse {
    constructor(data: Stock, errorDescription: ErrorDescription): this(data, listOf(errorDescription))
}