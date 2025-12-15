package com.github.watching1981.app.spring.controllers
import com.github.watching1981.api.v2.models.AdDeleteRequest
import com.github.watching1981.api.v2.models.AdDeleteResponse
import com.github.watching1981.api.v2.models.AdSearchRequest
import com.github.watching1981.api.v2.models.AdSearchResponse
import com.github.watching1981.api.v2.models.AdUpdateRequest
import com.github.watching1981.api.v2.models.AdUpdateResponse
import org.springframework.web.bind.annotation.*
import com.github.watching1981.app.spring.config.MkplAppSettings
import com.github.watching1981.api.v2.mappers.*
import com.github.watching1981.api.v2.models.*
import com.github.watching1981.api.v2.models.IRequest
import com.github.watching1981.app.common.controllerHelper
import com.github.watching1981.app.spring.controllers.AdControllerV2Fine.Companion

import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v2/ad")
class AdControllerV2Fine(private val appSettings: MkplAppSettings) {


    @PostMapping("create")
    suspend fun create(@RequestBody request: com.github.watching1981.api.v2.models.AdCreateRequest): com.github.watching1981.api.v2.models.AdCreateResponse =
        AdControllerV2Fine.process(appSettings, request = request, this::class, "create")


    @PostMapping("get")
    suspend fun  get(@RequestBody request: com.github.watching1981.api.v2.models.AdGetRequest): com.github.watching1981.api.v2.models.AdGetResponse =
        AdControllerV2Fine.process(appSettings, request = request, this::class, "get")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun  update(@RequestBody request: AdUpdateRequest): AdUpdateResponse =
        AdControllerV2Fine.process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun  delete(@RequestBody request: AdDeleteRequest): AdDeleteResponse =
        AdControllerV2Fine.process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun  search(@RequestBody request: AdSearchRequest): AdSearchResponse =
        AdControllerV2Fine.process(appSettings, request = request, this::class, "search")



    companion object {
        suspend inline fun <reified Q : com.github.watching1981.api.v2.models.IRequest, reified R : Any> process(
            appSettings: MkplAppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                fromTransport(request as com.github.watching1981.api.v2.models.IRequest)
            },
            { toTransportAd() as R },
            clazz,
            logId,
        )
    }
}
