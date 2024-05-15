package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugMode
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugStub
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.model.Stock
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import kotlin.test.Test

class SerializationTest {

    @Test
    fun `serialize and deserialize`() {
        val consumerStrategy = ConsumerStrategyImpl()
        val request = CreateStockRequest(
            requestType = "create",
            debug = com.otus.otuskotlin.stocktrack.api.v1.models.Debug(
                mode = DebugMode.PROD,
                stub = DebugStub.SUCCESS
            ),
            body = CreateStockBody(
                name = "Test Stock",
                category = StockCategory.SHARE
            )
        )
        val expected = SingleStockResponseContext(
            command = Command.CREATE,
            state = State.NONE,
            request = Stock(
                name = "Test Stock",
                category = Stock.Category.SHARE
            ),
            response = Stock(),
            debug = Debug(
                mode = Debug.Mode.PROD,
                stub = Debug.Stub.SUCCESS
            )
        )

        val deserializedContext = consumerStrategy.deserialize(request)

        assertThat(deserializedContext)
            .usingRecursiveComparison()
            .ignoringFields("request.id", "response.id", "requestId")
            .isEqualTo(expected)
    }
}