package com.github.watching1981.api.v2
import com.github.watching1981.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class RequestV2SerializationTest {


    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
    }

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


    @Test
    fun serialize() {
        val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)

        println(json)

        assertContains(json, Regex("\"title\":\\s*\"Toyota Camry 2019 года в отличном состоянии\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(request)
        val obj = apiV2Mapper.decodeFromString<IRequest>(json) as AdCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun `serialize all request types`() {
        val requests = listOf(
            request,
            AdGetRequest( id = 123),
            AdSearchRequest(
                brand = "Toyota",
                minYear = 2015,
                maxYear = 2020,
                minPrice = 1000000.0,
                maxPrice = 2000000.0,
                page = 0,
                propertySize = 20
            ),
            AdUpdateRequest(
                id = 123,
                lock = "lock-123",
                title = "Обновленное название",
                description = "Обновленное описание",
                price = 1400000.0
            ),
            AdDeleteRequest(
                id = 123,
                lock = "lock-123"
            )
        )

        println("=== ALL REQUEST TYPES KMP ===")
        requests.forEach { request ->
            val jsonString = json.encodeToString(request)
            println(jsonString)
            println("---")
        }
        println("=============================")
    }

}
