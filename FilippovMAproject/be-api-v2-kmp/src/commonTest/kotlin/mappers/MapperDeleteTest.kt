package com.github.watching1981.api.v2.mappers
import com.github.watching1981.api.v2.models.*
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue



class MapperDeleteTest {
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
    fun fromTransport() {
        val request = AdDeleteRequest(
            id = 999,
            lock = "lock-456"
        )
        val context = MkplContext()
        context.fromTransport(request)

        assertEquals(MkplCommand.DELETE, context.command)
        assertEquals(MkplAdvertisementId(999), context.adRequest.id)
    }

    @Test
    fun toTransport() {
        val context = MkplContext().apply {
            command = MkplCommand.DELETE
            adResponse = createSampleAdvertisement()
        }

        val response = context.toAdDeleteResponse()

        assertEquals(null, response.result)
        assertEquals(0, response.errors?.size ?: 0)
        assertEquals(123, response.deletedAdId)
        assertTrue(response.deletionTime!!.isNotBlank())
    }

}
