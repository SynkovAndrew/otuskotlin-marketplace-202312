package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.Debug
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugMode
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugStub
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.ResponseResult
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksFilter
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.api.v1.models.StockId
import com.otus.otuskotlin.stocktrack.api.v1.models.StockPermission
import com.otus.otuskotlin.stocktrack.api.v1.models.StockResponseBody
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockResponse
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StockApiModelsSerializationTest {
    private val stockClient = StockClient()

    @Test
    fun `create stock`() {
        runBlocking {
            val response = stockClient.createStock(
                CreateStockRequest(
                    requestType = "create",
                    debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                    body = CreateStockBody(
                        name = "Test Stock",
                        category = StockCategory.SHARE
                    )
                )
            )

            assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields("body.id")
                .isEqualTo(
                    CreateStockResponse(
                        responseType = "create",
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        body = StockResponseBody(
                            name = "Test Stock",
                            category = StockCategory.SHARE,
                            permissions = hashSetOf(StockPermission.READ, StockPermission.WRITE),
                            id = StockId("")
                        )
                    )
                )
        }
    }

    @Test
    fun `update stock`() {
        runBlocking {
            val response = stockClient.updateStock(
                UpdateStockRequest(
                    requestType = "update",
                    debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                    body = UpdateStockBody(
                        id = StockId(value = "234"),
                        name = "Test Stock",
                        category = StockCategory.SHARE,
                        lock = "lock-1"
                    )
                )
            )

            assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(
                    UpdateStockResponse(
                        responseType = "update",
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        body = StockResponseBody(
                            name = "Test Stock",
                            category = StockCategory.SHARE,
                            permissions = hashSetOf(StockPermission.READ, StockPermission.WRITE),
                            id = StockId("234"),
                            lock = "lock-1"
                        )
                    )
                )
        }
    }

    @Test
    fun `delete stock`() {
        runBlocking {
            val response = stockClient.deleteStock(
                DeleteStockRequest(
                    requestType = "delete",
                    debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                    body = DeleteStockBody(
                        id = StockId(value = "234"),
                        lock = "lock-1"
                    )
                )
            )

            assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(
                    DeleteStockResponse(
                        responseType = "delete",
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        body = StockResponseBody(
                            name = "Test Stock",
                            category = StockCategory.SHARE,
                            permissions = hashSetOf(StockPermission.READ, StockPermission.WRITE),
                            id = StockId("234"),
                            lock = "lock-1"
                        )
                    )
                )
        }
    }

    @Test
    fun `find stock`() {
        runBlocking {
            val response = stockClient.findStock(
                FindStockRequest(
                    requestType = "find",
                    debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                    body = FindStockBody(
                        id = StockId(value = "234")
                    )
                )
            )

            assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(
                    FindStockResponse(
                        responseType = "find",
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        body = StockResponseBody(
                            name = "Test Stock 234",
                            category = StockCategory.BOND,
                            permissions = hashSetOf(StockPermission.READ, StockPermission.WRITE),
                            id = StockId("234"),
                        )
                    )
                )
        }
    }

    @Test
    fun `search stocks`() {
        runBlocking {
            val response = stockClient.searchStocks(
                SearchStocksRequest(
                    requestType = "search",
                    debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                    filter = SearchStocksFilter("12345")
                )
            )

            assertThat(response)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(
                    SearchStocksResponse(
                        responseType = "search",
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        body = listOf(
                            StockResponseBody(
                                name = "Test Stock 1",
                                category = StockCategory.SHARE,
                                permissions = hashSetOf(StockPermission.READ, StockPermission.WRITE),
                                id = StockId("1"),
                            ),
                            StockResponseBody(
                                name = "Test Stock 2",
                                category = StockCategory.BOND,
                                permissions = hashSetOf(StockPermission.READ, StockPermission.WRITE),
                                id = StockId("2"),
                            )
                        )
                    )
                )
        }
    }
}