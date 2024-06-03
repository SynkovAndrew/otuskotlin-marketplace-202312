package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.model.StockLock
import com.otus.otuskotlin.stocktrack.stock.BaseStockRepository
import com.otus.otuskotlin.stocktrack.stock.OkStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkStocksRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.StockFilterRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockIdRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.StocksRepositoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

class PostgreSqlStockRepository(
    val randomUuid: () -> String = { UUID.randomUUID().toString() },
    properties: PostgreSqlProperties
) : BaseStockRepository() {

    private val connection = Database.connect(
        properties.url,
        properties.driver,
        properties.user,
        properties.password
    )

    override suspend fun create(request: StockRepositoryRequest): StockRepositoryResponse {
        return tryReturningOne {
            transactionWrapper {
                StockTable
                    .insert { toTable(it, request.stock, randomUuid) }
                    .resultedValues
                    ?.map { StockTable.fromTable(it) }
                    ?.single()
                    ?.let { OkStockRepositoryResponse(it) }
                    ?: notSingleResultErrorResponse(request.stock.id)
            }
        }
    }

    override suspend fun findById(request: StockIdRepositoryRequest): StockRepositoryResponse {
        return tryReturningOne {
            transactionWrapper { tryFindById(request.stockId) }
        }
    }

    override suspend fun update(request: StockRepositoryRequest): StockRepositoryResponse {
        return tryReturningOne {
            transactionWrapper {
                val stock = tryFindById(request.stock.id)

                when {
                    stock is OkStockRepositoryResponse && request.stock.lock.value != stock.data.lock.value ->
                        concurrencyErrorResponse(request.stock.id)

                    else -> {
                        StockTable
                            .update(
                                where = { StockTable.id eq request.stock.id.value },
                                body = { toTable(it, request.stock.copy(lock = StockLock(randomUuid())), randomUuid) }
                            )
                            .let { tryFindById(request.stock.id) }
                    }
                }
            }
        }
    }

    override suspend fun delete(request: StockIdRepositoryRequest): StockRepositoryResponse {
        return tryReturningOne {
            transactionWrapper {
                val stock = tryFindById(request.stockId)

                when {
                    stock is OkStockRepositoryResponse && request.lock.value != stock.data.lock.value ->
                        concurrencyErrorResponse(request.stockId)

                    else -> {
                        StockTable
                            .deleteWhere { id eq request.stockId.value }
                            .let { stock }
                    }
                }
            }
        }
    }

    override suspend fun search(request: StockFilterRepositoryRequest): StocksRepositoryResponse {
        return tryReturningMultiple {
            transactionWrapper {
                StockTable.selectAll()
                    .where {
                        buildList {
                            add(Op.TRUE)
                            request.name?.let { add(StockTable.name like "%$it%") }
                            request.category?.takeIf { it != Stock.Category.NONE }
                                ?.let { add(StockTable.category eq it.name) }
                        }.reduce { acc, op -> acc and op }
                    }
                    .map { StockTable.fromTable(it) }
                    .let { OkStocksRepositoryResponse(data = it) }
            }
        }
    }

    override fun enrich(stocks: Collection<Stock>): Collection<Stock> {
        return stocks
            .map { stock -> runBlocking { create(StockRepositoryRequest(stock)) } }
            .map { (it as OkStockRepositoryResponse).data }
    }

    private suspend inline fun <T> transactionWrapper(
        crossinline block: () -> T
    ): T {
        return withContext(Dispatchers.IO) {
            transaction(connection) {
                block()
            }
        }
    }

    private fun tryFindById(stockId: Stock.Id): StockRepositoryResponse {
        return StockTable.selectAll()
            .where { StockTable.id eq stockId.value }
            .singleOrNull()
            ?.let { OkStockRepositoryResponse(data = StockTable.fromTable(it)) }
            ?: stockNotFoundErrorResponse(stockId)
    }
}