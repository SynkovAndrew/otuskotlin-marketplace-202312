package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.Repository
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.Stock

sealed interface StocksRepositoryResponse : Repository.Response<List<Stock>>

data class OkStocksRepositoryResponse(
    val data: List<Stock>
) : StocksRepositoryResponse

data class ErrorStocksRepositoryResponse(
    val errors: List<ErrorDescription>
) : StocksRepositoryResponse {
    constructor(errorDescription: ErrorDescription): this(listOf(errorDescription))
}