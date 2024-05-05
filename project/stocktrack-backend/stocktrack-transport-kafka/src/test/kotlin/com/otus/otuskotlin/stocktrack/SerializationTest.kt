package com.otus.otuskotlin.stocktrack

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

        val contextJson = """
            {
                "type": "com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest",
                "requestType": "create",
                "debug": {
                    "mode": "prod",
                    "stub": "success"
                }
                "body": {
                    "name": "Test Stock",
                    "category": "SHARE"
                }
            }""".trimIndent()

        val deserializedContext = consumerStrategy.deserialize(contextJson)

        assertThat(deserializedContext)
            .usingRecursiveComparison()
            .ignoringFields("request.id", "response.id", "requestId")
            .isEqualTo(expected)
    }
}