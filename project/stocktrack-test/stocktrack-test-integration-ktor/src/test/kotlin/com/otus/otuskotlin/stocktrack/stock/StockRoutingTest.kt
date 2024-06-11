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
import com.otus.otuskotlin.stocktrack.api.v1.models.Error
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.ResponseResult
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksFilter
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.api.v1.models.StockId
import com.otus.otuskotlin.stocktrack.api.v1.models.StockResponseBody
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockResponse
import com.otus.otuskotlin.stocktrack.modules
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class StockRoutingTest {

    @Test
    fun `find stock successfully`() {
        testApplication {
            application { modules() }

            val stock = storeStock("Gazprom", StockCategory.SHARE)

            val response = configuredHttpClient().post {
                url("/api/v1/stock/find")
                contentType(ContentType.Application.Json)
                setBody(
                    FindStockRequest(
                        requestType = "find",
                        debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                        body = FindStockBody(id = stock.id)
                    )
                )
            }
            val findStockResponse = response.body<FindStockResponse>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertThat(findStockResponse)
                .usingRecursiveComparison()
                .isEqualTo(
                    FindStockResponse(
                        responseType = "find",
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        body = StockResponseBody(
                            id = stock.id,
                            name = "Gazprom",
                            category = StockCategory.SHARE,
                            permissions = emptySet(),
                            lock = stock.lock
                        )
                    )
                )
        }
    }

    @Test
    fun `failed to find not existing stock`() {
        testApplication {
            application { modules() }

            val response = configuredHttpClient().post {
                url("/api/v1/stock/find")
                contentType(ContentType.Application.Json)
                setBody(
                    FindStockRequest(
                        requestType = "find",
                        debug = Debug(mode = DebugMode.PROD, stub = DebugStub.NONE),
                        body = FindStockBody(id = StockId(value = "11"))
                    )
                )
            }
            val findStockResponse = response.body<FindStockResponse>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertThat(findStockResponse)
                .usingRecursiveComparison()
                .isEqualTo(
                    FindStockResponse(
                        responseType = "find",
                        result = ResponseResult.ERROR,
                        errors = listOf(
                            Error(
                                code = "stock-not-found",
                                field = "",
                                group = "",
                                message = "Stock(id=11) is not found"
                            )
                        ),
                        body = StockResponseBody(
                            id = StockId(value = ""),
                            name = "",
                            category = StockCategory.NONE,
                            permissions = setOf(),
                            lock = ""
                        )
                    )
                )
        }
    }

    @Test
    fun `create stock successfully`() {
        testApplication {
            application { modules() }

            val response = configuredHttpClient().post {
                url("/api/v1/stock/create")
                contentType(ContentType.Application.Json)
                setBody(
                    CreateStockRequest(
                        requestType = "create",
                        debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                        body = CreateStockBody(
                            name = "Uzim Co",
                            category = StockCategory.SHARE
                        )
                    )
                )
            }
            val createStockResponse = response.body<CreateStockResponse>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertThat(createStockResponse)
                .usingRecursiveComparison()
                .ignoringFields("body.id", "body.lock")
                .isEqualTo(
                    CreateStockResponse(
                        responseType = "create",
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        body = StockResponseBody(
                            id = StockId(""),
                            name = "Uzim Co",
                            category = StockCategory.SHARE,
                            permissions = emptySet(),
                            lock = ""
                        )
                    )
                )
        }
    }

    @Test
    fun `update stock successfully`() {
        testApplication {
            application { modules() }

            val stock = storeStock("Uzim Co", StockCategory.SHARE)

            val response = configuredHttpClient().post {
                url("/api/v1/stock/update")
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateStockRequest(
                        requestType = "update",
                        debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                        body = UpdateStockBody(
                            id = stock.id,
                            name = "Uzim Co",
                            category = StockCategory.SHARE,
                            lock = stock.lock!!
                        )
                    )
                )
            }
            val updateStockResponse = response.body<UpdateStockResponse>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertThat(updateStockResponse)
                .usingRecursiveComparison()
                .ignoringFields("body.lock")
                .isEqualTo(
                    UpdateStockResponse(
                        responseType = "update",
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        body = StockResponseBody(
                            id = stock.id,
                            name = "Uzim Co",
                            category = StockCategory.SHARE,
                            permissions = emptySet(),
                            lock = ""
                        )
                    )
                )
        }
    }

    @Test
    fun `failed to update not existing stock`() {
        testApplication {
            application { modules() }

            val response = configuredHttpClient().post {
                url("/api/v1/stock/update")
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateStockRequest(
                        requestType = "find",
                        debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                        body = UpdateStockBody(
                            id = StockId(value = "12"),
                            name = "Uzim Co",
                            category = StockCategory.SHARE,
                            lock = "lock-2"
                        )
                    )
                )
            }
            val updateStockResponse = response.body<UpdateStockResponse>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertThat(updateStockResponse)
                .usingRecursiveComparison()
                .isEqualTo(
                    UpdateStockResponse(
                        responseType = "update",
                        result = ResponseResult.ERROR,
                        errors = listOf(
                            Error(
                                code = "stock-not-found",
                                field = "",
                                group = "",
                                message = "Stock(id=12) is not found"
                            )
                        ),
                        body = StockResponseBody(
                            id = StockId(value = ""),
                            name = "",
                            category = StockCategory.NONE,
                            permissions = setOf(),
                            lock = ""
                        )
                    )
                )
        }
    }

    @Test
    fun `delete stock successfully`() {
        testApplication {
            application { modules() }

            val stock = storeStock("Rosbank", StockCategory.BOND)

            val response = configuredHttpClient().post {
                url("/api/v1/stock/delete")
                contentType(ContentType.Application.Json)
                setBody(
                    DeleteStockRequest(
                        requestType = "delete",
                        debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                        body = DeleteStockBody(
                            id = stock.id,
                            lock = stock.lock!!
                        )
                    )
                )
            }
            val deleteStockResponse = response.body<DeleteStockResponse>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertThat(deleteStockResponse)
                .usingRecursiveComparison()
                .isEqualTo(
                    DeleteStockResponse(
                        responseType = "delete",
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        body = StockResponseBody(
                            id = stock.id,
                            name = "Rosbank",
                            category = StockCategory.BOND,
                            permissions = emptySet(),
                            lock = stock.lock
                        )
                    )
                )
        }
    }

    @Test
    fun `search stocks successfully`() {
        testApplication {
            application { modules() }

            val stock = storeStock(UUID.randomUUID().toString(), StockCategory.BOND)

            val response = configuredHttpClient().post {
                url("/api/v1/stock/search")
                contentType(ContentType.Application.Json)
                setBody(
                    SearchStocksRequest(
                        requestType = "search",
                        debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                        filter = SearchStocksFilter(
                            searchString = stock.name
                        )
                    )
                )
            }
            val searchStocksResponse = response.body<SearchStocksResponse>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertThat(searchStocksResponse)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(
                    SearchStocksResponse(
                        responseType = "search",
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        body = listOf(
                            StockResponseBody(
                                id = stock.id,
                                name = stock.name,
                                category = stock.category,
                                permissions = emptySet(),
                                lock = stock.lock
                            )
                        )
                    )
                )
        }
    }

    private suspend fun ApplicationTestBuilder.storeStock(name: String, category: StockCategory): StockResponseBody {
        return configuredHttpClient()
            .post {
                url("/api/v1/stock/create")
                contentType(ContentType.Application.Json)
                setBody(
                    CreateStockRequest(
                        requestType = "create",
                        debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                        body = CreateStockBody(
                            name = name,
                            category = category
                        )
                    )
                )
            }
            .body<CreateStockResponse>()
            .body
    }
}
