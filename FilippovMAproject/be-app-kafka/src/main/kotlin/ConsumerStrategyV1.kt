package com.github.watching1981.app.kafka

import com.github.watching1981.api.v1.apiV1RequestDeserialize
import com.github.watching1981.api.v1.apiV1ResponseSerialize
import com.github.watching1981.api.v1.models.IRequest
import com.github.watching1981.api.v1.models.*
import com.github.watching1981.common.models.*
import com.github.watching1981.common.MkplContext
import com.github.watching1981.mappers.v1.*




class ConsumerStrategyV1 : IConsumerStrategy {
        override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }
    override fun serialize(source: MkplContext): String {
        val response: Any = when (source.command) {
            MkplCommand.CREATE -> source.toAdCreateResponse()
            MkplCommand.GET -> source.toAdGetResponse()
            MkplCommand.SEARCH -> source.toAdSearchResponse()
            MkplCommand.UPDATE -> source.toAdUpdateResponse()
            MkplCommand.DELETE -> source.toAdDeleteResponse()
            MkplCommand.NONE -> throw IllegalStateException("No command specified in context")
        }

        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: MkplContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }

    }


