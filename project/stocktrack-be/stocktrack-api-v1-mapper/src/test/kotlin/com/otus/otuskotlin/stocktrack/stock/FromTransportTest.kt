package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.Debug
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugMode
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugStub
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.Request
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksFilter
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.api.v1.models.StockId
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockRequest
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Stock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import com.otus.otuskotlin.stocktrack.model.Debug as InternalDebug

class FromTransportTest {

    @Test
    fun testSearchStocksRequestFromTransportModel() {
        val createStockRequest = SearchStocksRequest(
            type = "search",
            debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
            filter = SearchStocksFilter(
                searchString = "Test Stock"
            )
        )
        val responseContext = createStockRequest.fromTransportModel()

        assertEquals(Command.SEARCH, responseContext.command)
        assertEquals("Test Stock", responseContext.request.searchString)
        assertEquals(
            InternalDebug(mode = InternalDebug.Mode.PROD, stub = InternalDebug.Stub.SUCCESS),
            responseContext.debug
        )
    }

    @Test
    fun testCreateStockRequestFromTransportModel() {
        val createStockRequest = CreateStockRequest(
            type = "create",
            debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
            body = CreateStockBody(
                name = "Test Stock",
                category = StockCategory.SHARE
            )
        )
        val responseContext = createStockRequest.fromTransportModel()

        assertEquals(Command.CREATE, responseContext.command)
        assertEquals("Test Stock", responseContext.request.name)
        assertEquals(Stock.Category.SHARE, responseContext.request.category)
        assertEquals(
            InternalDebug(mode = InternalDebug.Mode.PROD, stub = InternalDebug.Stub.SUCCESS),
            responseContext.debug
        )
    }

    @Test
    fun testUpdateStockRequestFromTransportModel() {
        val updateStockRequest = UpdateStockRequest(
            type = "update",
            debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
            body = UpdateStockBody(
                name = "Updated Stock",
                category = StockCategory.BOND,
                id = StockId(value = "123"),
                lock = "lock"
            )
        )
        val responseContext = updateStockRequest.fromTransportModel()

        assertEquals(Command.UPDATE, responseContext.command)
        assertEquals("Updated Stock", responseContext.request.name)
        assertEquals(Stock.Category.BOND, responseContext.request.category)
        assertEquals(Stock.Id(value = "123"), responseContext.request.id)
        assertEquals("lock", responseContext.request.lock.value)
        assertEquals(
            InternalDebug(mode = InternalDebug.Mode.PROD, stub = InternalDebug.Stub.SUCCESS),
            responseContext.debug
        )
    }

    @Test
    fun testDeleteStockRequestFromTransportModel() {
        val deleteStockRequest = DeleteStockRequest(
            type = "delete",
            debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
            body = DeleteStockBody(
                id = StockId(value = "456"),
                lock = "lock"
            )
        )
        val responseContext = deleteStockRequest.fromTransportModel()

        assertEquals(Command.DELETE, responseContext.command)
        assertEquals(Stock.Id(value = "456"), responseContext.request.id)
        assertEquals("lock", responseContext.request.lock.value)
        assertEquals(
            InternalDebug(mode = InternalDebug.Mode.PROD, stub = InternalDebug.Stub.SUCCESS),
            responseContext.debug
        )
    }

    @Test
    fun testFindStockRequestFromTransportModel() {
        val findStockRequest = FindStockRequest(
            type = "find",
            debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
            body = FindStockBody(
                id = StockId(value = "789")
            )
        )
        val responseContext = findStockRequest.fromTransportModel()

        assertEquals(Command.FIND, responseContext.command)
        assertEquals(Stock.Id(value = "789"), responseContext.request.id)
        assertEquals(
            InternalDebug(mode = InternalDebug.Mode.PROD, stub = InternalDebug.Stub.SUCCESS),
            responseContext.debug
        )
    }

    @Test
    fun testInvalidRequestFromTransportModel() {
        val invalidRequest = object : Request {
            override val type: String = "invalid"
        }

        assertFailsWith<IllegalArgumentException> {
            invalidRequest.fromTransportModel()
        }
    }
}
