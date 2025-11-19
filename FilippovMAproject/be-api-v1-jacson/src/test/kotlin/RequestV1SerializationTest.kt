package com.github.watching1981.api.v1

import com.github.watching1981.api.v1.models.IRequest
import com.github.watching1981.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals


class RequestV1SerializationTest {
    private val request = AdCreateRequest(
        requestType = "create",
        title = "Toyota Camry 2018 года в отличном состоянии",
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


    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        assertContains(json, Regex("\"title\":\\s*\"Toyota Camry 2018 года в отличном состоянии\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {

        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json,IRequest::class.java)
        assertEquals(request.requestType, obj.requestType)
        assertEquals(request, obj)
    }


}

