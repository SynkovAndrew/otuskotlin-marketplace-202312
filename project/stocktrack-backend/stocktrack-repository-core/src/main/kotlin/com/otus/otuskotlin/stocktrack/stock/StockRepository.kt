package com.otus.otuskotlin.stocktrack.stock

interface StockRepository {

    suspend fun create(request: StockRepositoryRequest): StockRepositoryResponse

    suspend fun findById(request: StockIdRepositoryRequest): StockRepositoryResponse

    suspend fun update(request: StockRepositoryRequest): StockRepositoryResponse

    suspend fun delete(request: StockIdRepositoryRequest): StockRepositoryResponse

    suspend fun search(request: StockFilterRepositoryRequest): StocksRepositoryResponse
}
