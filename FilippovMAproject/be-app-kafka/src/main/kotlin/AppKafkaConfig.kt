package com.github.watching1981.app.kafka

import com.github.watching1981.app.common.IMkplAppSettings

import com.github.watching1981.backend.repo.postgresql.RepoAdSql
import com.github.watching1981.backend.repo.postgresql.SqlProperties
import com.github.watching1981.backend.repository.inmemory.AdRepoStub
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.car.logging.common.MpLoggerProvider
import com.github.watching1981.common.repo.IRepoAd
import com.github.watching1981.logging.jvm.mpLoggerLogback
import com.github.watching1981.repo.inmemory.AdRepoInMemory

data class PostgresConfig(
    val host: String,
    val port: Int,
    val database: String,
    val user: String,
    val password: String,
    val schema: String = "public",
    val table: String = "mkpl_advertisements"
) {
    // Конвертация в SqlProperties, если нужно
    fun toSqlProperties(): SqlProperties = SqlProperties(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema,
        table = table
    )
}
class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicInV1: String = KAFKA_TOPIC_IN_V1,
    val kafkaTopicOutV1: String = KAFKA_TOPIC_OUT_V1,
    // Создаем конфигурацию PostgreSQL из переменных окружения
    private val postgresConfig: PostgresConfig = PostgresConfig(
        host = System.getenv("MKPLADS_HOST") ?: "psql",
        port = (System.getenv("MKPLADS_PORT") ?: "5432").toInt(),
        database = System.getenv("MKPLADS_DB") ?: "cars_ads",
        user = System.getenv("MKPLADS_USER") ?: "postgres",
        password = System.getenv("MKPLADS_PASS") ?: "car-pass",
        schema = System.getenv("MKPLADS_SCHEMA") ?: "public",
        table = System.getenv("MKPLADS_TABLE") ?: "mkpl_advertisements"
    ),


    override val corSettings: MkplCorSettings = MkplCorSettings(
        loggerProvider = MpLoggerProvider { mpLoggerLogback(it) },
        repoTest =  AdRepoInMemory(),
        repoProd = RepoAdSql(postgresConfig.toSqlProperties()),
        repoStub = AdRepoStub(),
    ),
    override val processor: MkplAdProcessor = MkplAdProcessor(corSettings),
): IMkplAppSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
        const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
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
    }
}
