package com.github.watching1981.api.v2.mappers
import com.github.watching1981.api.v2.models.*
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals


class MapperUpdateTest {
    private val sampleCarInfo = CarInfo(
        brand = "Toyota",
        model = "Camry",
        year = 2020,
        mileage = 50000,
        color = "Black",
        engineType = EngineType.GASOLINE,
        engineVolume = 2.5,
        transmission = Transmission.AUTOMATIC
    )
    @Test
    fun fromTransport()  {
        val request = AdUpdateRequest(
            id = 789,
            lock = "lock-123",
            title = "Updated Title",
            description = "Updated Description",
            price = 1600000.0,
            carInfo = sampleCarInfo
        )

        val context = MkplContext()
        context.fromTransport(request)

        assertEquals(MkplCommand.UPDATE, context.command)
        assertEquals(MkplAdvertisementId(789), context.adRequest.id)
        assertEquals("Updated Title", context.adRequest.title)
        assertEquals("Updated Description", context.adRequest.description)
        assertEquals(1600000.0, context.adRequest.price)
        assertEquals("Toyota", context.adRequest.car.brand)
    }
    private fun createSampleAdvertisement(): MkplAdvertisement = MkplAdvertisement(
        id = MkplAdvertisementId(123),
        title = "Test Car",
        description = "Test Description",
        price = 1500000.0,
        car = MkplCar(
            brand = "Toyota",
            model = "Camry",
            year = 2020,
            mileage = 50000,
            color = "Black",
            engine = MkplEngine(MkplEngineType.GASOLINE, 2.5, 200),
            transmission = MkplTransmission.AUTOMATIC
        ),
        status = McplAdvertisementStatus.ACTIVE,
        location = "Moscow",
        contactPhone = "+79991234567",
        authorId = MkplUserId(123),
    )
    @Test
    fun toTransport()  {
        val context = MkplContext().apply {
            command = MkplCommand.UPDATE
            adResponse = createSampleAdvertisement().copy(
                title = "Updated Car",
                price = 1700000.0
            )
        }

        val response = context.toAdUpdateResponse()

        assertEquals(null, response.result)
        assertEquals(0, response.errors?.size ?: 0)
        assertEquals("Updated Car", response.ad?.title)
        assertEquals(1700000.0, response.ad?.price)
    }
}
