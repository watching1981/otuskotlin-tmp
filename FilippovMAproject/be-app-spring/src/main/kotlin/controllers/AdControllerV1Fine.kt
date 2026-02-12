package com.github.watching1981.app.spring.controllers

import org.springframework.web.bind.annotation.*
import com.github.watching1981.app.spring.config.MkplAppSettings
import com.github.watching1981.api.v1.models.*
import com.github.watching1981.api.v1.models.IRequest
import com.github.watching1981.app.common.controllerHelper
import com.github.watching1981.common.models.MkplWorkMode
import com.github.watching1981.mappers.v1.*
import kotlin.reflect.KClass


@Suppress("unused")
@RestController
@RequestMapping("v1/ad") //общая часть url эндпоинта. К ней будут добавляться части, которые конкретно отвечают за десвие с
//объявлением. Например v1/ad/create - запрос к эндпоинту на создание объявления. Все части описаны ниже
class AdControllerV1Fine(
    private val appSettings: MkplAppSettings
) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: AdCreateRequest): AdCreateResponse =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("get")
    suspend fun  get(@RequestBody request: AdGetRequest): AdGetResponse =
        process(appSettings, request = request, this::class, "get")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun  update(@RequestBody request: AdUpdateRequest): AdUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun  delete(@RequestBody request: AdDeleteRequest): AdDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun  search(@RequestBody request: AdSearchRequest): AdSearchResponse =
        process(appSettings, request = request, this::class, "search")



    companion object {
        suspend inline fun <reified Q : IRequest, reified R : Any> process(
            appSettings: MkplAppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                fromTransport(request as IRequest) //обогащение контекста полями из запроса
            },
            { toTransportAd() as R }, //преобразование получившегося контекста в ответ (нужен чтоб понимать успешно ли выполнились
            // действия над контекстом, которые запускаются в controllerHelper)
            clazz,
            logId,
        )
    }
}

