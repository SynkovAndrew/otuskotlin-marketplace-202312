package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.stock.EnrichableStockRepository
import com.otus.otuskotlin.stocktrack.stock.ErrorStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkStocksRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.stock.StockFilterRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockIdRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertIs

class PostgresStockRepositoryTest  {
    private val uuid = UUID.randomUUID().toString()
    private val repository: EnrichableStockRepository = PostgreSqlStockRepository(
        randomUuid = { uuid },
        properties = PostgreSqlProperties()
    )

    @Test
    fun `create stock successfully`() {
        runTest {
            val response = repository.create(
                StockRepositoryRequest(
                    Stock(
                        id = Stock.Id.NONE,
                        name = "Gazprom",
                        category = Stock.Category.SHARE,
                        permissions = emptySet()
                    )
                )
            )

            assertIs<OkStockRepositoryResponse>(response)
            assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields("data.lock")
                .isEqualTo(
                    OkStockRepositoryResponse(
                        data = Stock(
                            id = Stock.Id(value = uuid),
                            name = "Gazprom",
                            category = Stock.Category.SHARE,
                            permissions = emptySet()
                        )
                    )
                )

            val found = repository.findById(StockIdRepositoryRequest(stockId = response.data.id))

            assertThat(found)
                .usingRecursiveComparison()
                .ignoringFields("data.lock")
                .isEqualTo(
                    OkStockRepositoryResponse(
                        data = Stock(
                            id = Stock.Id(value = uuid),
                            name = "Gazprom",
                            category = Stock.Category.SHARE,
                            permissions = emptySet()
                        )
                    )
                )

            repository.delete(
                StockIdRepositoryRequest(
                    stockId = response.data.id,
                    lock = response.data.lock
                )
            )
        }
    }

    @Test
    fun `update stock successfully`() {
        runTest {
            val createResponse = repository.create(
                StockRepositoryRequest(
                    Stock(
                        id = Stock.Id.NONE,
                        name = "Gazprom",
                        category = Stock.Category.SHARE,
                        permissions = emptySet()
                    )
                )
            )

            assertIs<OkStockRepositoryResponse>(createResponse)

            val updatedStock = createResponse.data
                .copy(
                    name = "Gazprom Updated",
                    category = Stock.Category.BOND
                )
            val updateResponse = repository.update(StockRepositoryRequest(updatedStock))

            assertIs<OkStockRepositoryResponse>(updateResponse)
            assertThat(updateResponse)
                .usingRecursiveComparison()
                .ignoringFields("data.lock")
                .isEqualTo(
                    OkStockRepositoryResponse(
                        data = Stock(
                            id = Stock.Id(value = uuid),
                            name = "Gazprom Updated",
                            category = Stock.Category.BOND,
                            permissions = emptySet()
                        )
                    )
                )

            val found = repository.findById(StockIdRepositoryRequest(stockId = updatedStock.id))

            assertThat(found)
                .usingRecursiveComparison()
                .ignoringFields("data.lock")
                .isEqualTo(
                    OkStockRepositoryResponse(
                        data = Stock(
                            id = Stock.Id(value = uuid),
                            name = "Gazprom Updated",
                            category = Stock.Category.BOND,
                            permissions = emptySet()
                        )
                    )
                )

            repository.delete(
                StockIdRepositoryRequest(
                    stockId = createResponse.data.id,
                    lock = createResponse.data.lock
                )
            )
        }
    }

    @Test
    fun `delete stock successfully`() {
        runTest {
            val createResponse = repository.create(
                StockRepositoryRequest(
                    Stock(
                        id = Stock.Id.NONE,
                        name = "Gazprom",
                        category = Stock.Category.SHARE,
                        permissions = emptySet()
                    )
                )
            )

            assertIs<OkStockRepositoryResponse>(createResponse)
            val stockId = createResponse.data.id

            val deleteResponse = repository.delete(
                StockIdRepositoryRequest(stockId = stockId, lock = createResponse.data.lock)
            )
            assertIs<OkStockRepositoryResponse>(deleteResponse)

            val found = repository.findById(StockIdRepositoryRequest(stockId = stockId))
            assertThat(found)
                .usingRecursiveComparison()
                .isEqualTo(
                    ErrorStockRepositoryResponse(
                        errorDescription = ErrorDescription(
                            code = "stock-not-found",
                            message = "Stock(id=$uuid) is not found"
                        )
                    )
                )
        }
    }

    @Test
    fun `find stock successfully`() {
        runTest {
            val createResponse = repository.create(
                StockRepositoryRequest(
                    Stock(
                        id = Stock.Id.NONE,
                        name = "Gazprom",
                        category = Stock.Category.SHARE,
                        permissions = emptySet()
                    )
                )
            )

            assertIs<OkStockRepositoryResponse>(createResponse)
            val stockId = createResponse.data.id
            val found = repository.findById(StockIdRepositoryRequest(stockId = stockId))

            assertThat(found)
                .usingRecursiveComparison()
                .ignoringFields("data.lock")
                .isEqualTo(
                    OkStockRepositoryResponse(
                        data = Stock(
                            id = stockId,
                            name = "Gazprom",
                            category = Stock.Category.SHARE,
                            permissions = emptySet()
                        )
                    )
                )

            repository.delete(
                StockIdRepositoryRequest(
                    stockId = createResponse.data.id,
                    lock = createResponse.data.lock
                )
            )
        }
    }

    @Test
    fun `search stocks successfully`() {
        runTest {
            val stocks = setOf(
                Stock(
                    id = Stock.Id(value = "1"),
                    name = "Gazprom Ltd.",
                    category = Stock.Category.SHARE,
                    permissions = emptySet()
                ),
                Stock(
                    id = Stock.Id(value = "2"),
                    name = "Gazprom Co.",
                    category = Stock.Category.BOND,
                    permissions = emptySet()
                ),
                Stock(
                    id = Stock.Id(value = "3"),
                    name = "Alfabank",
                    category = Stock.Category.BOND,
                    permissions = emptySet()
                )
            )
            repository.enrich(stocks)

            val found = repository.search(
                StockFilterRepositoryRequest(
                    name = "Gazprom Ltd.",
                    category = Stock.Category.NONE
                )
            )

            assertThat(found)
                .usingRecursiveComparison()
                .ignoringFields("data.lock")
                .isEqualTo(
                    OkStocksRepositoryResponse(
                        data = listOf(
                            Stock(
                                id = Stock.Id(value = "1"),
                                name = "Gazprom Ltd.",
                                category = Stock.Category.SHARE,
                                permissions = emptySet()
                            )
                        )
                    )
                )

            stocks.forEach {
                repository.delete(
                    StockIdRepositoryRequest(
                        stockId = it.id,
                        lock = it.lock
                    )
                )
            }
        }
    }
}