package com.otus.otuskotlin.stocktrack

import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.lang.IllegalStateException
import java.time.Duration
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

class KafkaClient(
    private val kafkaApplicationSettings: KafkaApplicationSettings,
    private val consumer: KafkaConsumer<String, String> = kafkaApplicationSettings.instantiateKafkaConsumer(),
    private val producer: KafkaProducer<String, String> = kafkaApplicationSettings.instantiateKafkaProducer(),
    consumerStrategies: List<ConsumerStrategy>,
) {
    private val log: LoggerWrapper = kafkaApplicationSettings.coreSettings.loggerProvider.logger(this::class)
    private val active = AtomicBoolean(true)

    private val inputTopicToConsumerStrategy: Map<String, Pair<BidirectionalTopic, ConsumerStrategy>> =
        consumerStrategies
            .associate {
                it.topic(kafkaApplicationSettings)
                    .let { topic -> topic.input to (topic to it) }
            }

    fun start() = runBlocking { startSuspend() }

    private suspend fun startSuspend() {
        active.set(true)
        consumer.subscribe(inputTopicToConsumerStrategy.keys)

        while (active.get()) {
            val records = consumer.poll(Duration.ofMillis(1000))

            records.takeIf { !it.isEmpty }
                ?.also { log.info("{} records consumed", it.count()) }
                ?: log.info("No records consumed")

            records.forEach { record ->
                val (topic, strategy) = inputTopicToConsumerStrategy[record.topic()]
                    ?: throw IllegalStateException("Unsupported topic ${record.topic()}")

                val context = strategy.deserialize(record.value())
                val result = kafkaApplicationSettings.processSingleStockResponseContext(context, this::class)
                val payload = strategy.serialize(result)

                send(topic.output, payload)
            }
        }

    }

    private fun send(payload: String, topic: String) {
        ProducerRecord(topic, UUID.randomUUID().toString(), payload)
            .also { log.info("sending ${it.value()} to ${it.topic()}") }
            .also { producer.send(it) }
    }
}