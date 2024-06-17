package com.otus.otuskotlin.stocktrack.stock

class StubStockRepository : StockRepository {
    override suspend fun create(request: StockRepositoryRequest): StockRepositoryResponse {
        return OkStockRepositoryResponse(data = request.stock)
    }

    override suspend fun findById(request: StockIdRepositoryRequest): StockRepositoryResponse {
        return OkStockRepositoryResponse(
            data = Stock(
                id = request.stockId,
                name = "Test Stock",
                category = Stock.Category.SHARE
            )
        )
    }

    override suspend fun update(request: StockRepositoryRequest): StockRepositoryResponse {
        return OkStockRepositoryResponse(
            data = Stock(
                id = request.stock.id,
                name = request.stock.name,
                category = request.stock.category
            )
        )
    }

    override suspend fun delete(request: StockIdRepositoryRequest): StockRepositoryResponse {
        return OkStockRepositoryResponse(data = Stock(id = request.stockId))
    }

    override suspend fun search(request: StockFilterRepositoryRequest): StocksRepositoryResponse {
        return OkStocksRepositoryResponse(
            data = listOf(
                Stock(
                    id = Stock.Id(value = "1"),
                    name = "Gazprom",
                    category = Stock.Category.SHARE,
                    permissions = setOf(StockPermission.READ)
                )
            )
        )
    }
}
