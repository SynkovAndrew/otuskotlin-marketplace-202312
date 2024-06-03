package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.model.StockLock
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object StockTable: Table("public.stock") {
    val id = text("id")
    val name = text("name")
    val category = text("category")
    val lock = text("lock").nullable()

    override val primaryKey = PrimaryKey(id)

    fun fromTable(row: ResultRow): Stock {
        return Stock(
            id = Stock.Id(value = row[id]),
            name = row[name],
            category = Stock.Category.valueOf(row[category]),
            lock = row[lock]?.let { StockLock(value = it) } ?: StockLock.NONE,
        )
    }

    fun toTable(
        insertStatement: UpdateBuilder<Number>,
        stock: Stock,
        randomUuid: () -> String
    ) {
        insertStatement[id] = stock.id.value
            .takeIf { it.isNotBlank() }
            ?: randomUuid()
        insertStatement[name] = stock.name
        insertStatement[category] = stock.category.name
        insertStatement[lock] = stock.lock
            .takeIf { lock -> lock != StockLock.NONE }
            ?.value
            ?: randomUuid()
    }
}
