package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.snapshot.StockSnapshot
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object StockSnapshotTable: Table("public.stock_snapshot") {
    val id = text("id")
    val stockId = text("stock_id")
        .references(StockTable.id, onDelete = ReferenceOption.NO_ACTION)
    val value = decimal("value", 3, 2)
    val timestamp = timestamp("timestamp")

    override val primaryKey = PrimaryKey(id)

    fun fromTable(row: ResultRow): StockSnapshot {
        return StockSnapshot(
            id = StockSnapshot.Id(value = row[id]),
            stockId = Stock.Id(value = row[stockId]),
            value = row[value],
            timestamp = row[timestamp]
        )
    }

    fun toTable(
        insertStatement: UpdateBuilder<Number>,
        stockSnapshot: StockSnapshot,
        randomUuid: () -> String
    ) {
        insertStatement[id] = stockSnapshot.id.value
            .takeIf { it.isNotBlank() }
            ?: randomUuid()
        insertStatement[stockId] = stockSnapshot.stockId.value
        insertStatement[value] = stockSnapshot.value
        insertStatement[timestamp] = stockSnapshot.timestamp
    }
}
