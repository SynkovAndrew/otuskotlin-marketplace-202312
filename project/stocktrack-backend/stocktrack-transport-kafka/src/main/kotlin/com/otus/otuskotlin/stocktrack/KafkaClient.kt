package com.otus.otuskotlin.stocktrack

import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import java.lang.IllegalStateException
import java.time.Duration
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

class KafkaClient(
    private val kafkaApplicationSettings: KafkaApplicationSettings,
    private val cqrsBus: CQRSBus = CQRSBus(settings = kafkaApplicationSettings),
    private val consumer: Consumer<String, String> = kafkaApplicationSettings.instantiateKafkaConsumer(),
    private val producer: Producer<String, String> = kafkaApplicationSettings.instantiateKafkaProducer(),
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

    fun start() {
        runBlocking { startSuspend() }
    }

    fun stop() {
        active.set(false)
    }

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
                val result = cqrsBus.processSingleStockResponseContext(context)
                val payload = strategy.serialize(result)

                send(topic.output, payload)
            }
        }

    }

    private fun send(topic: String, payload: String) {
        ProducerRecord(topic, UUID.randomUUID().toString(), payload)
            .also { producer.send(it) }
            .also { log.info("${it.value()} send to topic ${it.topic()}") }
    }
}