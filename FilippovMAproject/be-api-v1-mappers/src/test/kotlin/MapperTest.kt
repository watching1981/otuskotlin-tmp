import org.junit.Test
import com.github.watching1981.api.v1.models.*
import com.github.watching1981.common.*
import com.github.watching1981.common.models.*
import com.github.watching1981.mappers.v1.fromTransportCreate
import kotlin.test.assertEquals
import com.github.watching1981.mappers.v1.toTransportAd


class MapperTest {
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
    private val sampleUserId = MkplUserId(123)

    @Test
    fun fromTransport() {
        val request = AdCreateRequest(
            requestType = "create",
            title = "Test Car",
            description = "Test Description",
            price = 1500000.0,
            carInfo = sampleCarInfo,
            location = "Tyumen",
            contactPhone = "+734521234567"
        )

        val context = MkplContext()
        context.fromTransportCreate(request)
        assertEquals(MkplCommand.CREATE, context.command)
        assertEquals("Test Car", context.adRequest.title)
        assertEquals("Test Description", context.adRequest.description)
        assertEquals("Toyota", context.adRequest.car.brand)
        assertEquals("Camry", context.adRequest.car.model)
        assertEquals(MkplEngineType.GASOLINE, context.adRequest.car.engine.type)
        assertEquals(MkplTransmission.AUTOMATIC, context.adRequest.car.transmission)
        assertEquals(McplAdvertisementStatus.DRAFT, context.adRequest.status)
        assertEquals("Tyumen", context.adRequest.location)
        assertEquals("+734521234567", context.adRequest.contactPhone)

    }
    private fun createSampleAdvertisement(): MkplAdvertisement = MkplAdvertisement(
        id = McplAdvertisementId(123),
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
    fun toTransport() {
        val context = MkplContext().apply {
            command = MkplCommand.CREATE
            adResponse = createSampleAdvertisement()
        }

       val response = context.toTransportAd()as AdCreateResponse

        assertEquals(null, response.result)
        assertEquals(0, response.errors?.size ?: 0)
        assertEquals("Test Car", response.ad?.title)
        assertEquals(123, response.adId)
    }

}
