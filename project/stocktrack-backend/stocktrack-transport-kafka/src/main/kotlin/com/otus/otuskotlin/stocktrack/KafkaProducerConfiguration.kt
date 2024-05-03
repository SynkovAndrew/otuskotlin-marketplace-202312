package com.otus.otuskotlin.stocktrack

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val producerProps = Properties().apply {
    putAll(
        mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.qualifiedName,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.qualifiedName,
            ProducerConfig.ACKS_CONFIG to "all",
        )
    )
}

val kafkaProducer = KafkaProducer<String, String>(producerProps)

suspend fun <K, V> Producer<K, V>.asyncSend(record: ProducerRecord<K, V>): RecordMetadata {
    return suspendCoroutine { continuation ->
        send(record) { metadata, exception ->
            exception
                ?.let(continuation::resumeWithException)
                ?: continuation.resume(metadata)
        }
    }
}
