package com.github.watching1981.app.common

import kotlinx.coroutines.test.runTest
import com.github.watching1981.api.v2.mappers.fromTransport
import com.github.watching1981.api.v2.mappers.toTransportAd
import com.github.watching1981.api.v2.models.*
import com.github.watching1981.app.common.IMkplAppSettings
import com.github.watching1981.app.common.controllerHelper
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.models.MkplAdLock
import com.github.watching1981.common.models.MkplWorkMode
import com.github.watching1981.common.stubs.MkplStubs
import kotlin.test.Test
import kotlin.test.assertEquals


class ControllerV2Test {



    private val request= AdCreateRequest(
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
            { fromTransport(request).apply { workMode =MkplWorkMode.STUB}.also { stubCase=MkplStubs.SUCCESS } },
            //{ fromTransport(request)},
            { toTransportAd() as AdCreateResponse },
            ControllerV2Test::class,
            "controller-v2-test"
        )




    @Test
    fun springHelperTest() = runTest {
        val res = createAdSpring(request as AdCreateRequest)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }


}
