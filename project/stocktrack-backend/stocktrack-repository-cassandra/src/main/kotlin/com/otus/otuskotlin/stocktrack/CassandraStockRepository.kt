package com.otus.otuskotlin.stocktrack

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.ResultSet
import com.datastax.oss.driver.api.core.cql.Row
import com.otus.otuskotlin.stocktrack.stock.BaseStockRepository
import com.otus.otuskotlin.stocktrack.stock.OkStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.stock.StockFilterRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockIdRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockLock
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.StocksRepositoryResponse
import kotlinx.coroutines.future.await
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.*

class CassandraStockRepository(private val cassandraProperties: CassandraProperties) : BaseStockRepository() {
    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(
                listOf(
                    InetSocketAddress(
                        InetAddress.getByName(cassandraProperties.host),
                        cassandraProperties.port
                    )
                )
            )
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(cassandraProperties.user, cassandraProperties.password)
            .withKeyspace("test_keyspace")
            .build()
    }

    private val mapper by lazy { StockDaoMapper.builder(session).build() }

    private val dao by lazy {
        mapper.stockDao("test_keyspace", StockEntity.TABLE_NAME)
    }

    override fun enrich(stocks: Collection<Stock>): Collection<Stock> {
        TODO("Not yet implemented")
    }

    override suspend fun create(request: StockRepositoryRequest): StockRepositoryResponse {
        val creating = request.stock
            .copy(
                id = Stock.Id(value = UUID.randomUUID().toString()),
                lock = StockLock(value = UUID.randomUUID().toString())
            )

        dao.create(StockEntity.fromInternal(creating)).await()

        return OkStockRepositoryResponse(data = creating)
    }

    override suspend fun findById(request: StockIdRepositoryRequest): StockRepositoryResponse {
        return dao.findById(request.stockId.value).await()
            ?.let { OkStockRepositoryResponse(data = it.toInternal()) }
            ?: stockNotFoundErrorResponse(request.stockId)
    }

    override suspend fun update(request: StockRepositoryRequest): StockRepositoryResponse {
        val updated = request.stock.copy(lock = StockLock(UUID.randomUUID().toString()))
        val result = dao.update(StockEntity.fromInternal(updated), request.stock.lock.value)
            .await()

        val succeed = result.wasApplied()
        val lock = result.one()
            ?.takeIf { it.columnDefinitions.contains(StockEntity.COLUMN_LOCK) }
            ?.getString(StockEntity.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }

        return when {
            succeed -> OkStockRepositoryResponse(data = updated)
            lock == null -> stockNotFoundErrorResponse(updated.id)
            else -> concurrencyErrorResponse(updated.id)
        }
    }

    override suspend fun delete(request: StockIdRepositoryRequest): StockRepositoryResponse {
        val deleting = (findById(StockIdRepositoryRequest(stockId = request.stockId)) as OkStockRepositoryResponse).data
        val result = dao.delete(request.stockId.value, request.lock.value)
            .await()

        return when {
            result.wasApplied() -> OkStockRepositoryResponse(data = deleting)
            else -> concurrencyErrorResponse(request.stockId)
        }

    }

    override suspend fun search(request: StockFilterRepositoryRequest): StocksRepositoryResponse {
        TODO("Not yet implemented")
    }
}