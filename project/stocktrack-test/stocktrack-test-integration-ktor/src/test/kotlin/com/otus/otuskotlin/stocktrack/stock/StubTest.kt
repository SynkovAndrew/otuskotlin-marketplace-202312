package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.Debug
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugMode
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugStub
import com.otus.otuskotlin.stocktrack.api.v1.models.Error
import com.otus.otuskotlin.stocktrack.api.v1.models.ResponseResult
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.api.v1.models.StockId
import com.otus.otuskotlin.stocktrack.api.v1.models.StockResponseBody
import com.otus.otuskotlin.stocktrack.modules
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
            application { modules() }

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
    fun `failed to create stock cause db error`() {
        testApplication {
            application { modules() }

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
            application { modules() }

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