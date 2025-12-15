

package com.github.watching1981.api.v1


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.watching1981.api.v1.models.IRequest


val apiV1Mapper = jacksonObjectMapper()
@Suppress("unused")
fun apiV1RequestSerialize(request: Any): String = apiV1Mapper.writeValueAsString(request)

@Suppress("unused")
fun <T : Any> apiV1RequestDeserialize(json: String, clazz: Class<T>): T =
    apiV1Mapper.readValue(json, clazz)


@Suppress("unused")
fun <T : IRequest> apiV1RequestDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IRequest::class.java) as T

@Suppress("unused")
fun apiV1ResponseSerialize(response: Any): String = apiV1Mapper.writeValueAsString(response)

@Suppress("unused")
fun <T : Any> apiV1ResponseDeserialize(json: String, clazz: Class<T>): T =
    apiV1Mapper.readValue(json, clazz)