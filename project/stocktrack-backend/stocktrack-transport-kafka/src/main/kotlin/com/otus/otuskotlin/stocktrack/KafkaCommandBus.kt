package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.api.v1.models.Request
import com.otus.otuskotlin.stocktrack.api.v1.models.Response
import com.otus.otuskotlin.stocktrack.serialization.RequestDeserializer
import com.otus.otuskotlin.stocktrack.serialization.ResponseSerializer
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.lang.IllegalStateException
import java.time.Duration
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

class KafkaCommandBus(
    private val kafkaApplicationSettings: KafkaApplicationSettings,
    private val commandBus: CommandBus = CommandBus(settings = kafkaApplicationSettings),
    private val consumer: Consumer<String, Request> = kafkaApplicationSettings.instantiateKafkaConsumer(
        StringDeserializer::class,
        RequestDeserializer::class
    ),
    private val producer: Producer<String, Response> = kafkaApplicationSettings.instantiateKafkaProducer(
        StringSerializer::class,
        ResponseSerializer::class
    ),
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
            val records: ConsumerRecords<String, Request> = consumer.poll(Duration.ofMillis(1000))

            records.takeIf { !it.isEmpty }
                ?.also { log.info("{} records consumed", it.count()) }
                ?: log.info("No records consumed")

            records.forEach { record ->
                val (topic, strategy) = inputTopicToConsumerStrategy[record.topic()]
                    ?: throw IllegalStateException("Unsupported topic ${record.topic()}")

                val context = strategy.deserialize(record.value())
                val result = commandBus.processSingleStockResponseContext(context)
                val payload = strategy.serialize(result)

                send(topic.output, payload)
            }
        }

    }

    private fun send(topic: String, payload: Response) {
        ProducerRecord(topic, UUID.randomUUID().toString(), payload)
            .also { producer.send(it) }
            .also { log.info("${it.value()} send to topic ${it.topic()}") }
    }
}