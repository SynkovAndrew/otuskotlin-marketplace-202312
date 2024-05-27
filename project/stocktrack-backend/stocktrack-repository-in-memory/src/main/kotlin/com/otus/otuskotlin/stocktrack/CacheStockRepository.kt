package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.stock.BaseStockRepository
import com.otus.otuskotlin.stocktrack.stock.ErrorStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkStocksRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.StockFilterRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockIdRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.StocksRepositoryResponse
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class CacheStockRepository(
    ttl: Duration = 2.minutes,
) : BaseStockRepository() {
    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, StockEntity>()
        .expireAfterWrite(ttl)
        .build()

    override suspend fun create(request: StockRepositoryRequest): StockRepositoryResponse {
        return tryReturningOne {
            request.stock.copy(id = Stock.Id(value = UUID.randomUUID().toString()))
                .also {
                    mutex.withLock("create") {
                        cache.put(it.id.value, StockEntityMapper.toEntity(it))
                    }
                }
                .let { OkStockRepositoryResponse(data = it) }
        }
    }

    override suspend fun findById(request: StockIdRepositoryRequest): StockRepositoryResponse {
        return tryReturningOne {
            request
                .let {
                    mutex.withLock("find") {
                        cache.get(it.stockId.value)
                    }
                }
                ?.let { OkStockRepositoryResponse(data = StockEntityMapper.fromEntity(it)) }
                ?: stockNotFoundErrorResponse(request.stockId)
        }
    }

    override suspend fun update(request: StockRepositoryRequest): StockRepositoryResponse {
        return tryReturningOne {
            request
                .let {
                    mutex.withLock("update") {
                        cache.get(it.stock.id.value)
                            ?.let {
                                StockEntityMapper.fromEntity(it)
                                    .copy(
                                        name = request.stock.name,
                                        category = request.stock.category
                                    )
                            }
                            ?.also { cache.put(it.id.value, StockEntityMapper.toEntity(it)) }
                    }
                }
                ?.let { OkStockRepositoryResponse(data = it) }
                ?: stockNotFoundErrorResponse(request.stock.id)
        }
    }

    override suspend fun delete(request: StockIdRepositoryRequest): StockRepositoryResponse {
        return tryReturningOne {
            request
                .let {
                    mutex.withLock("update") {
                        cache.get(it.stockId.value)
                            ?.also { cache.invalidate(it.id) }
                    }
                }
                ?.let { OkStockRepositoryResponse(data = StockEntityMapper.fromEntity(it)) }
                ?: stockNotFoundErrorResponse(request.stockId)
        }
    }

    override suspend fun search(request: StockFilterRepositoryRequest): StocksRepositoryResponse {
        return tryReturningMultiple {
            request
                .let {
                    cache.asMap().values
                        .filter { stock -> stock.category == it.category.name && stock.name.contains(it.name) }
                }
                .map { StockEntityMapper.fromEntity(it) }
                .let { OkStocksRepositoryResponse(data = it) }
        }
    }
}