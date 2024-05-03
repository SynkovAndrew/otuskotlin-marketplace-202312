package com.otus.otuskotlin.stocktrack

import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Duration
import kotlin.concurrent.thread

fun main() {
    thread(start = true) {
        (1..10).forEach {
            kafkaProducer
                .send(
                    ProducerRecord("test", "$it")
                )
                .get()
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