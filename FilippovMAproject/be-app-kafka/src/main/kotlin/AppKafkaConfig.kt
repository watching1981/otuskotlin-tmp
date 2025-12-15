package com.github.watching1981.app.kafka

import com.github.watching1981.app.common.IMkplAppSettings
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.car.logging.common.MpLoggerProvider
import com.github.watching1981.logging.jvm.mpLoggerLogback

class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicInV1: String = KAFKA_TOPIC_IN_V1,
    val kafkaTopicOutV1: String = KAFKA_TOPIC_OUT_V1,
    val kafkaTopicInV2: String = KAFKA_TOPIC_IN_V2,
    val kafkaTopicOutV2: String = KAFKA_TOPIC_OUT_V2,
    override val corSettings: MkplCorSettings = MkplCorSettings(
        loggerProvider = MpLoggerProvider { mpLoggerLogback(it) }
    ),
    override val processor: MkplAdProcessor = MkplAdProcessor(corSettings),
): IMkplAppSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
        const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
        const val KAFKA_TOPIC_IN_V2_VAR = "KAFKA_TOPIC_IN_V2"
        const val KAFKA_TOPIC_OUT_V2_VAR = "KAFKA_TOPIC_OUT_V2"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        //val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,; ]\\s*") }
        val KAFKA_HOSTS by lazy {
            val envValue = System.getenv(KAFKA_HOST_VAR)
            if (envValue.isNullOrEmpty()) {
                listOf("localhost:9092")
            } else {
                envValue.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            }
        }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "Car Market" }
        val KAFKA_TOPIC_IN_V1 by lazy { System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "cars-ad-v1-in" }
        val KAFKA_TOPIC_OUT_V1 by lazy { System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "cars-ad-v1-out" }
        val KAFKA_TOPIC_IN_V2 by lazy { System.getenv(KAFKA_TOPIC_IN_V2_VAR) ?: "cars-ad-v2-in" }
        val KAFKA_TOPIC_OUT_V2 by lazy { System.getenv(KAFKA_TOPIC_OUT_V2_VAR) ?: "cars-ad-v2-out" }
    }
}
