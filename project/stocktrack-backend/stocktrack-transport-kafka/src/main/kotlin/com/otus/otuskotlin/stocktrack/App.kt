package com.otus.otuskotlin.stocktrack

fun main() {
    val kafkaCommandBus = KafkaCommandBus(
        kafkaApplicationSettings = KafkaApplicationSettings(),
        consumerStrategies = listOf(ConsumerStrategyImpl())
    )
    kafkaCommandBus.start()
}