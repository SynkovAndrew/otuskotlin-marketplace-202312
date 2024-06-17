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

class ValidationTest {

    @Test
    fun `failed to find stock cause invalid id`() {
        testApplication {
            application { testModules() }

            val response = configuredHttpClient().post {
                url("/api/v1/stock/find")
                contentType(ContentType.Application.Json)
                setBody(
                    FindStockRequest(
                        requestType = "find",
                        debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                        body = FindStockBody(id = StockId(value = ""))
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
                                code = "ID_EMPTY",
                                group = "validation",
                                field = "id",
                                message = "Id is empty"
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
    fun `failed to delete stock cause invalid id`() {
        testApplication {
            application { testModules() }

            val response = configuredHttpClient().post {
                url("/api/v1/stock/delete")
                contentType(ContentType.Application.Json)
                setBody(
                    DeleteStockRequest(
                        requestType = "delete",
                        debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                        body = DeleteStockBody(id = StockId(value = ""), lock = "")
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
                        result = ResponseResult.ERROR,
                        errors = listOf(
                            Error(
                                code = "ID_EMPTY",
                                group = "validation",
                                field = "id",
                                message = "Id is empty"
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
    fun `failed to create stock cause invalid name`() {
        testApplication {
            application { testModules() }

            val response = configuredHttpClient().post {
                url("/api/v1/stock/create")
                contentType(ContentType.Application.Json)
                setBody(
                    CreateStockRequest(
                        requestType = "create",
                        debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                        body = CreateStockBody(name = "", category = StockCategory.SHARE)
                    )
                )
            }
            val createStockResponse = response.body<CreateStockResponse>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertThat(createStockResponse)
                .usingRecursiveComparison()
                .isEqualTo(
                    CreateStockResponse(
                        responseType = "create",
                        result = ResponseResult.ERROR,
                        errors = listOf(
                            Error(
                                code = "NAME_EMPTY",
                                group = "validation",
                                field = "name",
                                message = "Name is empty"
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
    fun `failed to update stock cause invalid name`() {
        testApplication {
            application { testModules() }

            val response = configuredHttpClient().post {
                url("/api/v1/stock/update")
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateStockRequest(
                        requestType = "create",
                        debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                        body = UpdateStockBody(
                            id = StockId("1"),
                            name = "",
                            category = StockCategory.SHARE,
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
                        result = ResponseResult.ERROR,
                        errors = listOf(
                            Error(
                                code = "NAME_EMPTY",
                                group = "validation",
                                field = "name",
                                message = "Name is empty"
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