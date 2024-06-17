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
import com.otus.otuskotlin.stocktrack.api.v1.models.StockPermission
import com.otus.otuskotlin.stocktrack.api.v1.models.StockResponseBody
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockResponse
import com.otus.otuskotlin.stocktrack.modules
import com.otus.otuskotlin.stocktrack.testModules
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlin.test.assertEquals

class StubTest {

    @Test
    fun `create stock successfully`() {
        testApplication {
            application { testModules() }

            val response = configuredHttpClient().post {
                url("/api/v1/stock/create")
                contentType(ContentType.Application.Json)
                setBody(
                    CreateStockRequest(
                        requestType = "create",
                        debug = Debug(mode = DebugMode.STUB, stub = DebugStub.SUCCESS),
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
                .ignoringFields("body.id")
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
    fun `find stock successfully`() {
        testApplication {
                        application { testModules() }


            val response = configuredHttpClient().post {
                url("/api/v1/stock/find")
                contentType(ContentType.Application.Json)
                setBody(
                    FindStockRequest(
                        requestType = "create",
                        debug = Debug(mode = DebugMode.STUB, stub = DebugStub.SUCCESS),
                        body = FindStockBody(id = StockId("1"))
                    )
                )
            }
            val findStockResponse = response.body<CreateStockResponse>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertThat(findStockResponse)
                .usingRecursiveComparison()
                .isEqualTo(
                    FindStockResponse(
                        responseType = "find",
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        body = StockResponseBody(
                            id = StockId("1"),
                            name = "Test Stock",
                            category = StockCategory.SHARE,
                            permissions = emptySet(),
                            lock = ""
                        )
                    )
                )
        }
    }

    @Test
    fun `delete stock successfully`() {
        testApplication {
            application { testModules() }

            val response = configuredHttpClient().post {
                url("/api/v1/stock/delete")
                contentType(ContentType.Application.Json)
                setBody(
                    DeleteStockRequest(
                        requestType = "delete",
                        debug = Debug(mode = DebugMode.STUB, stub = DebugStub.SUCCESS),
                        body = DeleteStockBody(id = StockId("1"), lock = "1")
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
                            id = StockId("1"),
                            name = "",
                            category = StockCategory.NONE,
                            permissions = emptySet(),
                            lock = "1"
                        )
                    )
                )
        }
    }

    @Test
    fun `update stock successfully`() {
        testApplication {
            application { testModules() }

            val response = configuredHttpClient().post {
                url("/api/v1/stock/update")
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateStockRequest(
                        requestType = "update",
                        debug = Debug(mode = DebugMode.STUB, stub = DebugStub.SUCCESS),
                        body = UpdateStockBody(
                            id = StockId("1"),
                            name = "New Stock",
                            category = StockCategory.BOND,
                            lock = "1"
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
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        body = StockResponseBody(
                            id = StockId("1"),
                            name = "New Stock",
                            category = StockCategory.BOND,
                            permissions = emptySet(),
                            lock = "1"
                        )
                    )
                )
        }
    }

    @Test
    fun `search stocks successfully`() {
        testApplication {
            application { testModules() }

            val response = configuredHttpClient().post {
                url("/api/v1/stock/search")
                contentType(ContentType.Application.Json)
                setBody(
                    SearchStocksRequest(
                        requestType = "update",
                        debug = Debug(mode = DebugMode.STUB, stub = DebugStub.SUCCESS),
                        filter = SearchStocksFilter(searchString = "")
                    )
                )
            }
            val searchStockResponse = response.body<SearchStocksResponse>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertThat(searchStockResponse)
                .usingRecursiveComparison()
                .isEqualTo(
                    SearchStocksResponse(
                        responseType = "search",
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        body = listOf(
                            StockResponseBody(
                                id = StockId("1"),
                                name = "Gazprom",
                                category = StockCategory.SHARE,
                                permissions = setOf(StockPermission.READ),
                                lock = ""
                            )
                        )
                    )
                )
        }
    }

    @Test
    fun `failed to create stock cause db error`() {
        testApplication {
            application { testModules() }

            val response = configuredHttpClient().post {
                url("/api/v1/stock/create")
                contentType(ContentType.Application.Json)
                setBody(
                    CreateStockRequest(
                        requestType = "create",
                        debug = Debug(mode = DebugMode.STUB, stub = DebugStub.DATABASE_ERROR),
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
                .ignoringFields("body.id")
                .isEqualTo(
                    CreateStockResponse(
                        responseType = "create",
                        result = ResponseResult.ERROR,
                        errors = listOf(
                            Error(
                                code = "db-error",
                                group = "db-error-group",
                                field = "no",
                                message = "Failed to access database"
                            )
                        ),
                        body = StockResponseBody(
                            id = StockId(""),
                            name = "",
                            category = StockCategory.NONE,
                            permissions = emptySet(),
                            lock = ""
                        )
                    )
                )
        }
    }

    @Test
    fun `failed to create stock cause stub not found`() {
        testApplication {
            application { testModules() }

            val response = configuredHttpClient().post {
                url("/api/v1/stock/create")
                contentType(ContentType.Application.Json)
                setBody(
                    CreateStockRequest(
                        requestType = "create",
                        debug = Debug(mode = DebugMode.STUB, stub = DebugStub.NONE),
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
                .ignoringFields("body.id")
                .isEqualTo(
                    CreateStockResponse(
                        responseType = "create",
                        result = ResponseResult.ERROR,
                        errors = listOf(
                            Error(
                                code = "stub-not-found-error",
                                group = "stub-not-found-error-group",
                                field = "no",
                                message = "Failed to find stub"
                            )
                        ),
                        body = StockResponseBody(
                            id = StockId(""),
                            name = "",
                            category = StockCategory.NONE,
                            permissions = emptySet(),
                            lock = ""
                        )
                    )
                )
        }
    }
}