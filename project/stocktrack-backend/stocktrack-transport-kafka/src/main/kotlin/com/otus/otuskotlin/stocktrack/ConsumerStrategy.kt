package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.api.v1.models.Request
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.stock.fromTransportModel
import com.otus.otuskotlin.stocktrack.stock.toTransportModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface ConsumerStrategy {

    fun topic(kafkaApplicationSettings: KafkaApplicationSettings): BidirectionalTopic

    fun serialize(context: SingleStockResponseContext): String

    fun deserialize(value: String): SingleStockResponseContext
}

data class BidirectionalTopic(
    val input: String,
    val output: String
)

class ConsumerStrategyImpl : ConsumerStrategy {
    override fun topic(kafkaApplicationSettings: KafkaApplicationSettings): BidirectionalTopic {
        return BidirectionalTopic(
            input = kafkaApplicationSettings.kafkaTopicIn,
            output = kafkaApplicationSettings.kafkaTopicOut
        )
    }

    override fun serialize(context: SingleStockResponseContext): String {
        return context.toTransportModel()
            .let { Json.encodeToString(it) }
    }

    override fun deserialize(value: String): SingleStockResponseContext {
        return Json
            .decodeFromString<Request>(value)
            .fromTransportModel()
    }
}