package com.github.watching1981.app.kafka

import com.github.watching1981.api.v1.apiV1ResponseSerialize
import com.github.watching1981.api.v2.apiV2RequestDeserialize
import com.github.watching1981.api.v2.apiV2ResponseSerialize
import com.github.watching1981.api.v2.mappers.fromTransport
import com.github.watching1981.api.v2.mappers.toTransportAd
import com.github.watching1981.api.v2.models.IRequest
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.MkplCommand
import com.github.watching1981.mappers.v1.*
import com.github.watching1981.api.v2.models.BaseResponse

class ConsumerStrategyV2 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV2, config.kafkaTopicOutV2)
    }

//    override fun serialize(source: MkplContext): String {
//        val response: IResponse = source.toTransportAd()
//        return apiV2ResponseSerialize(response)
//    }
override fun serialize(source: MkplContext): String {
    val response: Any = when (source.command) {
        MkplCommand.CREATE -> source.toAdCreateResponse()
        MkplCommand.GET -> source.toAdGetResponse()
        MkplCommand.SEARCH -> source.toAdSearchResponse()
        MkplCommand.UPDATE -> source.toAdUpdateResponse()
        MkplCommand.DELETE -> source.toAdDeleteResponse()
        MkplCommand.NONE -> throw IllegalStateException("No command specified in context")
    }

    return apiV2ResponseSerialize(response as BaseResponse)
}

    override fun deserialize(value: String, target: MkplContext) {
        val request: IRequest = apiV2RequestDeserialize(value)
        target.fromTransport(request)
    }
}
