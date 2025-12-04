package com.github.watching1981.app.kafka

import com.github.watching1981.common.MkplContext

/**
 * Интерфейс стратегии для обслуживания версии API
 */
interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: AppKafkaConfig): InputOutputTopics
    /**
     * Сериализатор для версии API
     */
    fun serialize(source: MkplContext): String
    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: MkplContext)
}
