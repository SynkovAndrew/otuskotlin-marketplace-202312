package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.stock.StockFilterRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockIdRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockRepository
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.StocksRepositoryResponse

class CacheStockRepository : StockRepository {
    override suspend fun create(request: StockRepositoryRequest): StockRepositoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun findById(request: StockIdRepositoryRequest): StockRepositoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun update(request: StockRepositoryRequest): StockRepositoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun delete(request: StockIdRepositoryRequest): StockRepositoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun search(request: StockFilterRepositoryRequest): StocksRepositoryResponse {
        TODO("Not yet implemented")
    }
}