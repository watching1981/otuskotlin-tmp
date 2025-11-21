@file:Suppress("unused")

package com.github.watching1981.api.v2

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import com.github.watching1981.api.v2.models.IRequest
import com.github.watching1981.api.v2.models.BaseResponse

@OptIn(ExperimentalSerializationApi::class)
@Suppress("JSON_FORMAT_REDUNDANT_DEFAULT")
val apiV2Mapper = Json {
    allowTrailingComma = true
}

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV2RequestDeserialize(json: String) =
    apiV2Mapper.decodeFromString<IRequest>(json) as T

fun apiV2ResponseSerialize(obj: BaseResponse): String =
    apiV2Mapper.encodeToString(BaseResponse.serializer(), obj)

@Suppress("UNCHECKED_CAST")
fun <T : BaseResponse> apiV2ResponseDeserialize(json: String) =
    apiV2Mapper.decodeFromString<BaseResponse>(json) as T

inline fun <reified T : BaseResponse> apiV2ResponseSimpleDeserialize(json: String) =
    apiV2Mapper.decodeFromString<T>(json)

@Suppress("unused")
fun apiV2RequestSerialize(obj: IRequest): String =
    apiV2Mapper.encodeToString(IRequest.serializer(), obj)

@Suppress("unused")
inline fun <reified T : IRequest> apiV2RequestSimpleSerialize(obj: T): String =
    apiV2Mapper.encodeToString<T>(obj)
