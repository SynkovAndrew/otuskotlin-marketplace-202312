package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.Debug
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugMode
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugStub
import com.otus.otuskotlin.stocktrack.api.v1.models.Request
import com.otus.otuskotlin.stocktrack.api.v1.models.ResponseResult
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.api.v1.models.StockId
import com.otus.otuskotlin.stocktrack.api.v1.models.StockResponseBody
import com.otus.otuskotlin.stocktrack.serialization.ResponseSerializer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import java.util.*
import kotlin.test.Test

class KafkaCommandBusTest {
    private val kafkaApplicationSettings: KafkaApplicationSettings = KafkaApplicationSettings()
    private val consumer = MockConsumer<String, Request>(OffsetResetStrategy.EARLIEST)
    private val producer = MockProducer(true, StringSerializer(), ResponseSerializer)
    private val consumerStrategy = ConsumerStrategyImpl()
    private val kafkaCommandBus = KafkaCommandBus(
        kafkaApplicationSettings = kafkaApplicationSettings,
        consumer = consumer,
        producer = producer,
        consumerStrategies = listOf(consumerStrategy)
    )
    private val inputTopic = kafkaApplicationSettings.kafkaTopicIn

    @Test
    fun `send message to kafka`() {
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

        consumer.schedulePollTask {
            consumer.rebalance(
                listOf(TopicPartition(inputTopic, 0))
            )
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    0,
                    0L,
                    UUID.randomUUID().toString(),
                    request
                )
            )
            kafkaCommandBus.stop()
        }

        val offsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val topicPartition = TopicPartition(inputTopic, 0)
        offsets[topicPartition] = 0L
        consumer.updateBeginningOffsets(offsets)

        kafkaCommandBus.start()

        val message = producer.history().first()

        assertThat(message.value())
            .usingRecursiveComparison()
            .ignoringFields("body.id")
            .isEqualTo(
                CreateStockResponse(
                    responseType = "create",
                    result = ResponseResult.SUCCESS,
                    errors = emptyList(),
                    body = StockResponseBody(
                        name = "Test Stock",
                        category = StockCategory.SHARE,
                        id = StockId("111"),
                        permissions = emptySet(),
                        lock = ""
                    )
                )
            )
    }
}