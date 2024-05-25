package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.Request
import com.otus.otuskotlin.stocktrack.api.v1.models.Response
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockResponse
import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.stock.fromTransportModel
import com.otus.otuskotlin.stocktrack.stock.toTransportModel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

interface ConsumerStrategy {

    fun topic(kafkaApplicationSettings: KafkaApplicationSettings): BidirectionalTopic

    fun serialize(context: Context<*, *>): Response

    fun deserialize(value: Request): Context<*, *>
}

data class BidirectionalTopic(
    val input: String,
    val output: String
)

object RequestSerializers : KSerializer<List<Request>> by ListSerializer(Request.serializer())

class ConsumerStrategyImpl : ConsumerStrategy {
    private val module = SerializersModule {
        polymorphic(Request::class) {
            subclass(CreateStockRequest::class)
            subclass(FindStockRequest::class)
            subclass(DeleteStockRequest::class)
            subclass(UpdateStockRequest::class)
            subclass(SearchStocksRequest::class)
        }
        polymorphic(Response::class) {
            subclass(CreateStockResponse::class)
            subclass(FindStockResponse::class)
            subclass(DeleteStockResponse::class)
            subclass(UpdateStockResponse::class)
            subclass(SearchStocksResponse::class)
        }
    }
    private val json = Json { serializersModule = module }

    override fun topic(kafkaApplicationSettings: KafkaApplicationSettings): BidirectionalTopic {
        return BidirectionalTopic(
            input = kafkaApplicationSettings.kafkaTopicIn,
            output = kafkaApplicationSettings.kafkaTopicOut
        )
    }

    override fun serialize(context: Context<*, *>): Response {
        return context.toTransportModel()
    }

    override fun deserialize(value: Request): Context<*, *> {
        return value.fromTransportModel()
    }
}