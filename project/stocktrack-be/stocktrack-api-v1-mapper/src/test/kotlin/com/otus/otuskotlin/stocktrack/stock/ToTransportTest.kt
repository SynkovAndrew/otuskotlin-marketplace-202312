package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.ResponseResult
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.api.v1.models.StockId
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockResponse
import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.RequestId
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.model.StockFilter
import com.otus.otuskotlin.stocktrack.model.StockLock
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import com.otus.otuskotlin.stocktrack.model.Debug as InternalDebug

class ToTransportTest {

    @Test
    fun testCreateStockResponseToTransportModel() {
        val responseContext = SingleStockResponseContext(
            command = Command.CREATE,
            request = Stock(
                name = "Test Stock",
                category = Stock.Category.SHARE,
                id = Stock.Id(value = "123"),
                lock = StockLock(value = "lock")
            ),
            response = Stock(
                name = "Created Stock",
                category = Stock.Category.SHARE,
                id = Stock.Id(value = "456"),
                lock = StockLock(value = "lock")
            ),
            state = State.FINISHED,
            errors = listOf(
                ErrorDescription(
                    message = "Error 1",
                    group = "Group 1",
                    field = "Field 1",
                    code = "Code 1"
                )
            ),
            debug = InternalDebug(mode = InternalDebug.Mode.PROD, stub = InternalDebug.Stub.SUCCESS),
            requestId = RequestId(value = "789"),
            startedAt = Instant.MAX
        )
        val response = responseContext.toTransportModel()

        assertIs<CreateStockResponse>(response)
        assertEquals(Command.CREATE.value, response.type)
        assertEquals(ResponseResult.SUCCESS, response.result)
        assertEquals("Created Stock", response.body.name)
        assertEquals(StockCategory.SHARE, response.body.category)
        assertEquals(StockId(value = "456"), response.body.id)
        assertEquals("lock", response.body.lock)
        assertEquals(1, response.errors.size)
        assertEquals("Error 1", response.errors[0].message)
        assertEquals("Code 1", response.errors[0].code)
        assertEquals("Field 1", response.errors[0].field)
        assertEquals("Group 1", response.errors[0].group)
    }

    @Test
    fun testInvalidCommandToTransportModel() {
        val invalidResponseContext = SingleStockResponseContext(
            command = Command.SEARCH,
            request = Stock.NONE,
            response = Stock.NONE,
            state = State.NONE,
            errors = emptyList(),
            debug = InternalDebug(mode = InternalDebug.Mode.PROD, stub = InternalDebug.Stub.SUCCESS)
        )

        assertFailsWith<IllegalArgumentException> {
            invalidResponseContext.toTransportModel()
        }
    }

    @Test
    fun testUpdateStockResponseToTransportModel() {
        val responseContext = SingleStockResponseContext(
            command = Command.UPDATE,
            request = Stock(
                name = "Test Stock",
                category = Stock.Category.SHARE,
                id = Stock.Id(value = "123"),
                lock = StockLock(value = "lock")
            ),
            response = Stock(
                name = "Updated Stock",
                category = Stock.Category.BOND,
                id = Stock.Id(value = "456"),
                lock = StockLock(value = "lock")
            ),
            state = State.FAILED,
            errors = listOf(
                ErrorDescription(
                    message = "Error 1",
                    group = "Group 1",
                    field = "Field 1",
                    code = "Code 1"
                )
            ),
            debug = InternalDebug(mode = InternalDebug.Mode.PROD, stub = InternalDebug.Stub.SUCCESS),
            requestId = RequestId(value = "789"),
            startedAt = Instant.MAX
        )
        val response = responseContext.toTransportModel()

        assertIs<UpdateStockResponse>(response)
        assertEquals(Command.UPDATE.value, response.type)
        assertEquals(ResponseResult.ERROR, response.result)
        assertEquals("Updated Stock", response.body.name)
        assertEquals(StockCategory.BOND, response.body.category)
        assertEquals(StockId(value = "456"), response.body.id)
        assertEquals("lock", response.body.lock)
        assertEquals(1, response.errors.size)
        assertEquals("Error 1", response.errors[0].message)
        assertEquals("Code 1", response.errors[0].code)
        assertEquals("Field 1", response.errors[0].field)
        assertEquals("Group 1", response.errors[0].group)
    }

    @Test
    fun testDeleteStockResponseToTransportModel() {
        val responseContext = SingleStockResponseContext(
            command = Command.DELETE,
            request = Stock(
                name = "Test Stock",
                category = Stock.Category.SHARE,
                id = Stock.Id(value = "123"),
                lock = StockLock(value = "lock")
            ),
            response = Stock(
                name = "Deleted Stock",
                category = Stock.Category.BOND,
                id = Stock.Id(value = "456"),
                lock = StockLock(value = "lock")
            ),
            state = State.RUNNING,
            errors = listOf(
                ErrorDescription(
                    message = "Error 1",
                    group = "Group 1",
                    field = "Field 1",
                    code = "Code 1"
                )
            ),
            debug = InternalDebug(mode = InternalDebug.Mode.PROD, stub = InternalDebug.Stub.SUCCESS),
            requestId = RequestId(value = "789"),
            startedAt = Instant.MAX
        )
        val response = responseContext.toTransportModel()

        assertIs<DeleteStockResponse>(response)
        assertEquals(Command.DELETE.value, response.type)
        assertEquals(ResponseResult.SUCCESS, response.result)
        assertEquals("Deleted Stock", response.body.name)
        assertEquals(StockCategory.BOND, response.body.category)
        assertEquals(StockId(value = "456"), response.body.id)
        assertEquals("lock", response.body.lock)
        assertEquals(1, response.errors.size)
        assertEquals("Error 1", response.errors[0].message)
        assertEquals("Code 1", response.errors[0].code)
        assertEquals("Field 1", response.errors[0].field)
        assertEquals("Group 1", response.errors[0].group)
    }

    @Test
    fun testFindStockResponseToTransportModel() {
        val responseContext = SingleStockResponseContext(
            command = Command.FIND,
            request = Stock(
                name = "Test Stock",
                category = Stock.Category.SHARE,
                id = Stock.Id(value = "123"),
                lock = StockLock(value = "lock")
            ),
            response = Stock(
                name = "Found Stock",
                category = Stock.Category.BOND,
                id = Stock.Id(value = "456"),
                lock = StockLock(value = "lock")
            ),
            state = State.FINISHED,
            errors = listOf(
                ErrorDescription(
                    message = "Error 1",
                    group = "Group 1",
                    field = "Field 1",
                    code = "Code 1"
                )
            ),
            debug = InternalDebug(mode = InternalDebug.Mode.PROD, stub = InternalDebug.Stub.SUCCESS),
            requestId = RequestId(value = "789"),
            startedAt = Instant.MAX
        )
        val response = responseContext.toTransportModel()

        assertIs<FindStockResponse>(response)
        assertEquals(Command.FIND.value, response.type)
        assertEquals(ResponseResult.SUCCESS, response.result)
        assertEquals("Found Stock", response.body.name)
        assertEquals(StockCategory.BOND, response.body.category)
        assertEquals(StockId(value = "456"), response.body.id)
        assertEquals("lock", response.body.lock)
        assertEquals(1, response.errors.size)
        assertEquals("Error 1", response.errors[0].message)
        assertEquals("Code 1", response.errors[0].code)
        assertEquals("Field 1", response.errors[0].field)
        assertEquals("Group 1", response.errors[0].group)
    }

    @Test
    fun testSearchStockResponseToTransportModel() {
        val responseContext = SearchStocksResponseContext(
            command = Command.SEARCH,
            request = StockFilter(),
            response = listOf(Stock(
                name = "Searched Stock",
                category = Stock.Category.BOND,
                id = Stock.Id(value = "456"),
                lock = StockLock(value = "lock")
            )),
            state = State.FINISHED,
            errors = listOf(
                ErrorDescription(
                    message = "Error 1",
                    group = "Group 1",
                    field = "Field 1",
                    code = "Code 1"
                )
            ),
            debug = InternalDebug(mode = InternalDebug.Mode.PROD, stub = InternalDebug.Stub.SUCCESS),
            requestId = RequestId(value = "789"),
            startedAt = Instant.MAX
        )
        val response = responseContext.toTransportModel()

        assertIs<SearchStocksResponse>(response)
        assertEquals(Command.SEARCH.value, response.type)
        assertEquals(ResponseResult.SUCCESS, response.result)
        assertEquals(1, response.body.size)
        assertEquals("Searched Stock", response.body[0].name)
        assertEquals(StockCategory.BOND, response.body[0].category)
        assertEquals(StockId(value = "456"), response.body[0].id)
        assertEquals("lock", response.body[0].lock)
        assertEquals(1, response.errors.size)
        assertEquals("Error 1", response.errors[0].message)
        assertEquals("Code 1", response.errors[0].code)
        assertEquals("Field 1", response.errors[0].field)
        assertEquals("Group 1", response.errors[0].group)
    }
}
