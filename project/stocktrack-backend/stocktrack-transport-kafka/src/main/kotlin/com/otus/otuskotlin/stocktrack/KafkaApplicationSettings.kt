package com.otus.otuskotlin.stocktrack

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer
import java.util.*
import kotlin.reflect.KClass

class KafkaApplicationSettings(
    val kafkaHosts: List<String> = listOf("localhost:9092"),
    val kafkaGroupId: String = "stocktrack",
    val kafkaTopicIn: String = "stocktrack-stock-in",
    val kafkaTopicOut: String = "stocktrack-stock-out",
    override val coreSettings: CoreSettings =
        CoreSettings(loggerProvider = LoggerProvider { logbackLoggerWrapper(it) }),
    override val singleStockResponseProcessor: SingleStockResponseProcessor =
        SingleStockResponseProcessor(coreSettings = coreSettings),
    override val searchStocksResponseProcessor: SearchStocksResponseProcessor =
        SearchStocksResponseProcessor(coreSettings = coreSettings)
) : ApplicationSettings

fun <K, V> KafkaApplicationSettings.instantiateKafkaConsumer(
    keyDeserializerClass: KClass<out Deserializer<*>>,
    valueDeserializerClass: KClass<out Deserializer<*>>,
): KafkaConsumer<K, V> {
    return KafkaConsumer<K, V>(
        Properties().apply {
            putAll(
                mapOf(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaHosts,
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to keyDeserializerClass.qualifiedName,
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to valueDeserializerClass.qualifiedName,
                    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to true,
                    ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG to 1000,
                    ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to 30000,
                    ConsumerConfig.GROUP_ID_CONFIG to "stocktrack"
                )
            )
        }
    )
}

fun <K, V> KafkaApplicationSettings.instantiateKafkaProducer(
    keySerializerClass: KClass<out Serializer<*>>,
    valueSerializerClass: KClass<out Serializer<*>>,
): KafkaProducer<K, V> {
    return KafkaProducer<K, V>(
        Properties().apply {
            putAll(
                mapOf(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaHosts,
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to keySerializerClass.qualifiedName,
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to valueSerializerClass.qualifiedName,
                    ProducerConfig.ACKS_CONFIG to "all",
                )
            )
        }
    )
}
