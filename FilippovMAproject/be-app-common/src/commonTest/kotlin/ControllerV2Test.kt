package com.github.watching1981.app.common

import kotlinx.coroutines.test.runTest
import com.github.watching1981.api.v2.mappers.fromTransport
import com.github.watching1981.api.v2.mappers.toTransportAd
import com.github.watching1981.api.v2.models.*
import com.github.watching1981.app.common.IMkplAppSettings
import com.github.watching1981.app.common.controllerHelper
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals


class ControllerV2Test {



    private val request: IRequest = AdCreateRequest(
        title = "Toyota Camry 2019 года в отличном состоянии",
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
    )

    private val appSettings: IMkplAppSettings = object : IMkplAppSettings {
        override val corSettings: MkplCorSettings = MkplCorSettings()
        override val processor: MkplAdProcessor = MkplAdProcessor(corSettings)
    }

    private suspend fun createAdSpring(request: AdCreateRequest): AdCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportAd() as AdCreateResponse },
            ControllerV2Test::class,
            "controller-v2-test"
        )

    class TestApplicationCall(private val request: IRequest) {
        var res: BaseResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: BaseResponse) {
            this.res = res
        }
    }

//    private suspend fun TestApplicationCall.createAdKtor(appSettings: IMkplAppSettings) {
//        val resp = appSettings.controllerHelper(
//            { fromTransport(receive<AdCreateRequest>()) },
//            { toTransportAd() },
//            ControllerV2Test::class,
//            "controller-v2-test"
//        )
//        respond(resp as BaseResponse)
//    }

    @Test
    fun springHelperTest() = runTest {
        val res = createAdSpring(request as AdCreateRequest)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

 //   @Test
//    fun ktorHelperTest() = runTest {
//        val testApp = TestApplicationCall(request).apply { createAdKtor(appSettings) }
//        val res = testApp.res as AdCreateResponse
//        assertEquals(ResponseResult.SUCCESS, res.result)
//    }
}
