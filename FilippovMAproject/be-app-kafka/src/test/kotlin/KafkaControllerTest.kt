package com.github.watching1981.app.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import com.github.watching1981.api.v1.apiV1RequestSerialize
import com.github.watching1981.api.v1.apiV1ResponseDeserialize
import com.github.watching1981.api.v1.models.AdCreateRequest
import com.github.watching1981.api.v1.models.AdCreateResponse
import com.github.watching1981.api.v1.models.CarInfo
import com.github.watching1981.api.v1.models.EngineType
import com.github.watching1981.api.v1.models.Transmission
import com.github.watching1981.marketplace.app.kafka.AppKafkaConsumer
import java.util.*
import kotlin.test.assertEquals


class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        AdCreateRequest(
                            title = "Toyota Camry 2020",
                            description = "Автомобиль в идеальном состоянии, один хозяин, полная сервисная история. Без ДТП.",
                            price = 1500000.0,
                            carInfo = CarInfo(
                                brand = "Toyota",
                                model = "Camry",
                                year = 2018,
                                mileage = 75000,
                                color = "Черный",
                                engineType = EngineType.GASOLINE,
                                engineVolume = 2.5,
                                transmission = Transmission.AUTOMATIC
                            ),
                            location = "Москва",
                            contactPhone = "+79991234567"
                        ),
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<AdCreateResponse>(message.value(), AdCreateResponse::class.java)
        assertEquals(outputTopic, message.topic())
        assertEquals("Toyota Camry 2020", result.ad?.title)
    }

    companion object {
        const val PARTITION = 0
    }
}


