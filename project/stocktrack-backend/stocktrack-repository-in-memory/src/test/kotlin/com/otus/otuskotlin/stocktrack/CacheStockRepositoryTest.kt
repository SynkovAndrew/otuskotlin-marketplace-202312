package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.stock.EnrichableStockRepository

class CacheStockRepositoryTest : BaseStockRepositoryTest() {
    override val repository: EnrichableStockRepository = CacheStockRepository(randomUuid = { uuid })
}