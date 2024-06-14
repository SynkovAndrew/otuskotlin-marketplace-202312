package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.stock.ErrorStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkStocksRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.stock.StockFilterRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockIdRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockLock
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CassandraStockRepositoryTest {
    private val repository = CassandraStockRepository(CassandraProperties())

    @BeforeTest
    @AfterTest
    fun `before test`() {
        clearAll()
    }

    @Test
    fun `create stock`() {
        runTest {
            val createResponse = storeStock("Test Stock", Stock.Category.SHARE)
            val found = repository.findById(StockIdRepositoryRequest(stockId = createResponse.data.id))
            assertIs<OkStockRepositoryResponse>(found)
            assertEquals("Test Stock", found.data.name)
            assertEquals(Stock.Category.SHARE, found.data.category)
        }
    }

    @Test
    fun `update stock`() {
        runTest {
            val createResponse = storeStock("Test Stock", Stock.Category.SHARE)

            val updated = repository.update(
                StockRepositoryRequest(
                    stock = Stock(
                        id = createResponse.data.id,
                        name = "New Stock",
                        category = Stock.Category.BOND,
                        lock = createResponse.data.lock
                    )
                )
            )
            assertIs<OkStockRepositoryResponse>(updated)
            assertTrue(updated.data.id != Stock.Id.NONE)
            assertTrue(updated.data.lock != StockLock.NONE)
            assertEquals("New Stock", updated.data.name)
            assertEquals(Stock.Category.BOND, updated.data.category)

            val found = repository.findById(StockIdRepositoryRequest(stockId = updated.data.id))
            assertIs<OkStockRepositoryResponse>(found)
            assertEquals("New Stock", found.data.name)
            assertEquals(Stock.Category.BOND, found.data.category)
        }
    }

    @Test
    fun `delete stock`() {
        runTest {
            val createResponse = storeStock("Test Stock", Stock.Category.SHARE)

            val deleted = repository.delete(
                StockIdRepositoryRequest(
                    stockId = createResponse.data.id,
                    lock = createResponse.data.lock,
                )
            )
            assertIs<OkStockRepositoryResponse>(deleted)
            assertTrue(deleted.data.id != Stock.Id.NONE)
            assertTrue(deleted.data.lock != StockLock.NONE)
            assertEquals("Test Stock", deleted.data.name)
            assertEquals(Stock.Category.SHARE, deleted.data.category)

            val found = repository.findById(StockIdRepositoryRequest(stockId = deleted.data.id))
            assertIs<ErrorStockRepositoryResponse>(found)
            assertEquals(
                ErrorDescription(
                    code = "stock-not-found",
                    message = "Stock(id=${createResponse.data.id.value}) is not found"
                ),
                found.errors[0]
            )
        }
    }

    @Test
    fun `search stocks`() {
        runTest {
            storeStock("Stock AAA", Stock.Category.SHARE)
            storeStock("Stock BBB", Stock.Category.SHARE)
            storeStock("Stock DDD", Stock.Category.SHARE)
            storeStock("Bond CCC", Stock.Category.BOND)

            val found1 = repository.search(StockFilterRepositoryRequest(name = "Stock DDD"))

            assertIs<OkStocksRepositoryResponse>(found1)
            assertThat(found1.data)
                .usingRecursiveComparison()
                .ignoringFields("id", "lock")
                .isEqualTo(
                    listOf(
                        Stock(name = "Stock DDD", category = Stock.Category.SHARE)
                    )
                )

            val found2 = repository.search(StockFilterRepositoryRequest(category = Stock.Category.BOND))

            assertIs<OkStocksRepositoryResponse>(found2)
            assertThat(found2.data)
                .usingRecursiveComparison()
                .ignoringFields("id", "lock")
                .isEqualTo(
                    listOf(
                        Stock(name = "Bond CCC", category = Stock.Category.BOND)
                    )
                )
        }
    }

    private fun clearAll() {
        runBlocking {
            (repository.search(StockFilterRepositoryRequest()) as OkStocksRepositoryResponse).data
                .forEach {
                    repository.delete(
                        StockIdRepositoryRequest(
                            stockId = it.id,
                            lock = it.lock
                        )
                    )
                }
        }
    }

    private suspend fun storeStock(name: String, category: Stock.Category): OkStockRepositoryResponse {
        val createResponse = repository.create(StockRepositoryRequest(Stock(name = name, category = category)))
        assertIs<OkStockRepositoryResponse>(createResponse)
        assertTrue(createResponse.data.id != Stock.Id.NONE)
        assertTrue(createResponse.data.lock != StockLock.NONE)
        assertEquals(name, createResponse.data.name)
        assertEquals(category, createResponse.data.category)
        return createResponse
    }
}