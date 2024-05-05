package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.Response
import com.otus.otuskotlin.stocktrack.api.v1.models.ResponseResult
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.api.v1.models.StockId
import com.otus.otuskotlin.stocktrack.api.v1.models.StockResponseBody
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import java.util.*
import kotlin.test.Test

class KafkaClientTest {
    private val kafkaApplicationSettings: KafkaApplicationSettings = KafkaApplicationSettings()
    private val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
    private val producer = MockProducer(true, StringSerializer(), StringSerializer())
    private val consumerStrategy = ConsumerStrategyImpl()
    private val kafkaClient = KafkaClient(
        kafkaApplicationSettings = kafkaApplicationSettings,
        consumer = consumer,
        producer = producer,
        consumerStrategies = listOf(consumerStrategy)
    )
    private val inputTopic = kafkaApplicationSettings.kafkaTopicIn

    @Test
    fun `send message to kafka`() {
        val incomingMessage = """
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
                    incomingMessage
                )
            )
            kafkaClient.stop()
        }

        val offsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val topicPartition = TopicPartition(inputTopic, 0)
        offsets[topicPartition] = 0L
        consumer.updateBeginningOffsets(offsets)

        kafkaClient.start()

        val message = producer.history().first()
        val decoded: Response = Json.decodeFromString<Response>(message.value())

        assertThat(decoded)
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