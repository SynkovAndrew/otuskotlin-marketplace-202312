package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.stock.EnrichableStockRepository

class PostgresStockRepositoryTest : BaseStockRepositoryTest() {
    override val repository: EnrichableStockRepository = PostgreSqlStockRepository(
        randomUuid = { uuid },
        properties = PostgreSqlProperties()
    )
}