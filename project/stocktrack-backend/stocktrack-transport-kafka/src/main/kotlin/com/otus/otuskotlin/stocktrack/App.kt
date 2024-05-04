package com.otus.otuskotlin.stocktrack

import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Duration
import java.util.UUID
import kotlin.concurrent.thread

fun main() {
    val kafkaApplicationSettings = KafkaApplicationSettings()
    val producer = kafkaApplicationSettings.instantiateKafkaProducer()
    KafkaClient(kafkaApplicationSettings, )
    thread(start = true) {
        (1..3).forEach {
            producer.send(
                ProducerRecord(
                    kafkaApplicationSettings.kafkaTopicIn,
                    UUID.randomUUID().toString(),
                    """
                        {
                            "requestType": "crate",
                            "debug": {
                                "mode": "PROD",
                                "stub": "SUCCESS"
                            },
                            "body": {
                                "name": "TEST STOCK $it",
                                "category": "SHARE"
                            }
                        }
                    """.trimIndent()
                    )
            )
        }
    }

    thread(start = true) {
        kafkaConsumer.subscribe(listOf("test"))
        kafkaConsumer
            .poll(Duration.ofSeconds(10))
            .forEach {
                println("partition=${it.partition()} value=${it.value()}")
            }
    }.join()
}