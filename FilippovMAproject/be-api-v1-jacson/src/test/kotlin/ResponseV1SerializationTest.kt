package com.github.watching1981.api.v1

import com.github.watching1981.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
//    private val response = AdResponse(
//        result = ResponseResult.SUCCESS,
//        errors = emptyList(),
//        timestamp = "2025-10-05T14:30:00Z",
//        id = 123,
//        lock = "lock-123456",
//        title = "Toyota Camry 2020 года в отличном состоянии",
//        description = "Автомобиль в идеальном состоянии, один хозяин, полная сервисная история. Без ДТП.",
//        price = 2000000.0,
//        carInfo = CarInfo(
//            brand = "Toyota",
//            model = "Camry",
//            year = 2018,
//            mileage = 75000,
//            color = "Черный",
//            engineType = EngineType.GASOLINE,
//            engineVolume = 2.5,
//            transmission = Transmission.AUTOMATIC
//        ),
//        status = AdStatus.ACTIVE,
//        location = "Тюмень",
//        contactPhone = "+79821234567",
//        createdAt = "2025-10-05T14:30:00Z"
//    )
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
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"title\":\\s*\"Toyota Camry 2020 года в отличном состоянии\""))
        assertContains(json, Regex("\"result\":\\s*\"success\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, AdGetResponse::class.java)
        assertEquals(response, obj)
    }
    @Test
    fun `test response with different statuses`() {
        val statuses = listOf(
            AdStatus.ACTIVE,
            AdStatus.DRAFT,
            AdStatus.SOLD,
            AdStatus.ARCHIVED
        )

        println("=== RESPONSE WITH DIFFERENT STATUSES ===")
        statuses.forEach { status ->
            val statusResponse = response.ad?.copy(status = status)
            val json = apiV1Mapper.writeValueAsString(statusResponse)
            println("Status: $status -> $json")
        }
        println("========================================")
    }
}
