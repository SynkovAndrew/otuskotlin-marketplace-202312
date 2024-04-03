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
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.model.Stock
import org.assertj.core.api.Assertions.assertThat
import java.util.*
import kotlin.test.Test
import com.otus.otuskotlin.stocktrack.model.Debug as InternalDebug

class SingleStockResponseMapperTest {

    @Test
    fun `from transport model`() {
        val request = CreateStockRequest(
            type = "create",
            debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
            body = CreateStockBody(
                name = "Test Stock",
                category = StockCategory.SHARE
            )
        )
        val expected = SingleStockResponseContext(
            command = Command.CREATE,
            request = Stock(name = "Test Stock", category = Stock.Category.SHARE),
            debug = InternalDebug(mode = InternalDebug.Mode.PROD, stub = InternalDebug.Stub.SUCCESS)
        )

        assertThat(request.fromTransportModel())
            .usingRecursiveComparison()
            .ignoringFields("requestId", "startedAt", "request.id")
            .isEqualTo(expected)
    }

    @Test
    fun `to transport model`() {
        val context = SingleStockResponseContext(
            command = Command.CREATE,
            state = State.RUNNING,
            errors = listOf(
                com.otus.otuskotlin.stocktrack.model.Error(
                    code = "BAD_REQUEST",
                    group = "GROUP",
                    field = "FIELD",
                    message = "Error"
                )
            ),
            request = Stock(name = "Test Stock", category = Stock.Category.SHARE),
            response = Stock(name = "Test Stock", category = Stock.Category.SHARE),
            debug = InternalDebug(mode = InternalDebug.Mode.PROD, stub = InternalDebug.Stub.SUCCESS)
        )

        val expected = CreateStockResponse(
            type = "create",
            result = ResponseResult.SUCCESS,
            errors = listOf(
                Error("BAD_REQUEST", "GROUP", "FIELD", "Error")
            ),
            body = StockResponseBody(
                name = "Test Stock",
                category = StockCategory.SHARE,
                id = StockId(UUID.randomUUID().toString()),
                permissions = emptySet(),
                lock = "lock"
            )
        )
        assertThat(context.toTransportModel())
            .usingRecursiveComparison()
            .ignoringFields("requestId", "startedAt", "request.id", "response.id", "body.id", "body.lock")
            .isEqualTo(expected)
    }
}