package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.snapshot.BaseStockSnapshotRepository
import com.otus.otuskotlin.stocktrack.snapshot.OkStockSnapshotRepositoryResponse
import com.otus.otuskotlin.stocktrack.snapshot.OkStockSnapshotsRepositoryResponse
import com.otus.otuskotlin.stocktrack.snapshot.StockSnapshot
import com.otus.otuskotlin.stocktrack.snapshot.StockSnapshotRepositoryResponse
import com.otus.otuskotlin.stocktrack.snapshot.StockSnapshotsRepositoryResponse
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class PostgreSqlSnapshotRepository(
    private val randomUuid: () -> String = { UUID.randomUUID().toString() },
    properties: PostgreSqlProperties
) : BaseStockSnapshotRepository() {

    private val database = Database.connect(
        properties.url,
        properties.driver,
        properties.user,
        properties.password
    )

    override suspend fun create(stockSnapshot: StockSnapshot): StockSnapshotRepositoryResponse {
        return tryReturningOne {
            transactionWrapper(database) {
                StockSnapshotTable
                    .insert { toTable(it, stockSnapshot, randomUuid) }
                    .resultedValues
                    ?.map { StockSnapshotTable.fromTable(it) }
                    ?.single()
                    ?.let { OkStockSnapshotRepositoryResponse(it) }
                    ?: notSingleResultErrorResponse(stockSnapshot.id)
            }
        }
    }

    override suspend fun findByStockId(id: Stock.Id): StockSnapshotsRepositoryResponse {
        return tryReturningMultiple {
            transactionWrapper(database) {
                StockSnapshotTable.selectAll()
                    .where { StockSnapshotTable.stockId eq id.value }
                    .map { StockSnapshotTable.fromTable(it) }
                    .let { OkStockSnapshotsRepositoryResponse(data = it) }
            }
        }
    }
}