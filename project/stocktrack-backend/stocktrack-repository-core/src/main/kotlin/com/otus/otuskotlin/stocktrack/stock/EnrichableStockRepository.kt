package com.otus.otuskotlin.stocktrack.stock

interface EnrichableStockRepository : StockRepository {
    fun enrich(stocks: Collection<Stock>): Collection<Stock>
}

class EnrichableStockRepositoryImpl(
    enrichWith: Collection<Stock>,
    private val enrichableStockRepository: EnrichableStockRepository
) : EnrichableStockRepository by enrichableStockRepository {
    val enrichedWith: List<Stock> = enrich(enrichWith).toList()
}