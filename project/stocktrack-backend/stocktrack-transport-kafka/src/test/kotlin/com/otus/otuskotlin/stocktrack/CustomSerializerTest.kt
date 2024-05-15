package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.Debug
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugMode
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugStub
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.serialization.RequestDeserializer
import com.otus.otuskotlin.stocktrack.serialization.RequestSerializer
import kotlin.test.Test
import kotlin.test.assertEquals

class CustomSerializerTest {

    @Test
    fun `serialize and deserialize`() {
        val request = CreateStockRequest(
            requestType = "create",
            debug = Debug(
                mode = DebugMode.PROD,
                stub = DebugStub.SUCCESS
            ),
            body = CreateStockBody(
                name = "Test Stock",
                category = StockCategory.SHARE
            )
        )
        val serialized = RequestSerializer.serialize("test", request)
        val deserialized = RequestDeserializer.deserialize("test", serialized)
        assertEquals(request, deserialized)
    }
}