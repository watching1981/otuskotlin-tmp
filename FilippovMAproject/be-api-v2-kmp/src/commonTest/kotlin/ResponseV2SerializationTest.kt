
package com.github.watching1981.api.v2
import com.github.watching1981.api.v2.models.*
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
    }


    private val sampleAdData = AdData(
        id = 123,
        lock = "lock-123456",
        title = "Toyota Camry 2020 года в отличном состоянии",
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
        status = AdStatus.ACTIVE,
        location = "Москва",
        contactPhone = "+79991234567",
        createdAt = "2025-10-05T14:30:00Z",
        updatedAt = "2025-10-05T14:30:00Z",
        viewsCount = 150,
        favoriteCount = 25
    )
    val response = AdGetResponse(
        result = ResponseResult.SUCCESS,
        errors = emptyList(),
        timestamp = "2025-10-05T14:30:00Z",
        ad = sampleAdData
    )


    @Test
    fun serialize () {
        val json = apiV2Mapper.encodeToString(response)
        println(json)
        assertContains(json, Regex("\"title\":\\s*\"Toyota Camry 2020 года в отличном состоянии\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(response)
        val obj = apiV2Mapper.decodeFromString<AdGetResponse>(json)
        assertEquals(response, obj)
    }
}
