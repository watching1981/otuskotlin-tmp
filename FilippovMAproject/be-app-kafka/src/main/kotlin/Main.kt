package com.github.watching1981.app.kafka

import com.github.watching1981.marketplace.app.kafka.AppKafkaConsumer

fun main() {

    val config = AppKafkaConfig()
    val consumer = AppKafkaConsumer(config, listOf(ConsumerStrategyV1(), ConsumerStrategyV2()))
    consumer.start()
}



