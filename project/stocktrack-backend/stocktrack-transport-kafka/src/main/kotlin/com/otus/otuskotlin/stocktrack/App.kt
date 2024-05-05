package com.otus.otuskotlin.stocktrack

import org.apache.kafka.clients.producer.ProducerRecord
import java.util.UUID
import kotlin.concurrent.thread

fun main() {
    val kafkaApplicationSettings = KafkaApplicationSettings()
    val producer = kafkaApplicationSettings.instantiateKafkaProducer()
    val kafkaClient = KafkaClient(
        kafkaApplicationSettings = kafkaApplicationSettings,
        consumerStrategies = listOf(ConsumerStrategyImpl())
    )
    thread(start = true) {
        kafkaClient.start()
    }

    thread(start = true) {
        (1..3).forEach {
            producer.send(
                ProducerRecord(
                    kafkaApplicationSettings.kafkaTopicIn,
                    UUID.randomUUID().toString(),
                    """
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
                    )
            )
        }
    }.join()
}