package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.stock.ErrorStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.stock.StockIdRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockLock
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CassandraStockRepositoryTest {
    private val repository = CassandraStockRepository(CassandraProperties())

    @Test
    fun `create stock`() {
        runTest {
            val createResponse = repository.create(
                StockRepositoryRequest(
                    Stock(name = "Test Stock", category = Stock.Category.SHARE)
                )
            )
            assertIs<OkStockRepositoryResponse>(createResponse)
            assertTrue(createResponse.data.id != Stock.Id.NONE)
            assertTrue(createResponse.data.lock != StockLock.NONE)
            assertEquals("Test Stock", createResponse.data.name)
            assertEquals(Stock.Category.SHARE, createResponse.data.category)

            val found = repository.findById(StockIdRepositoryRequest(stockId = createResponse.data.id))
            assertIs<OkStockRepositoryResponse>(found)
            assertEquals("Test Stock", found.data.name)
            assertEquals(Stock.Category.SHARE, found.data.category)
        }
    }

    @Test
    fun `update stock`() {
        runTest {
            val createResponse = repository.create(
                StockRepositoryRequest(
                    Stock(name = "Test Stock", category = Stock.Category.SHARE)
                )
            )
            assertIs<OkStockRepositoryResponse>(createResponse)

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
            val createResponse = repository.create(
                StockRepositoryRequest(
                    Stock(name = "Test Stock", category = Stock.Category.SHARE)
                )
            )
            assertIs<OkStockRepositoryResponse>(createResponse)

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
}