package com.github.watching1981.biz.repo

import kotlinx.coroutines.test.runTest
import com.github.watching1981.backend.repo.tests.AdRepositoryMock
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.models.*
import com.github.watching1981.common.repo.DbAdResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class BizRepoCreateTest {

    private val userId = MkplUserId(0)
    private val command = MkplCommand.CREATE
    private val repo = AdRepositoryMock(
        invokeCreateAd = {
            DbAdResponseOk(
                data = MkplAdvertisement(
                    id = MkplAdvertisementId(1), // Используем числовой ID вместо UUID
                    title = it.ad.title,
                    description = it.ad.description,
                    price = it.ad.price,
                    car = it.ad.car,
                    status = it.ad.status,
                    location = it.ad.location,
                    contactPhone = it.ad.contactPhone,
                    authorId = it.ad.authorId ?: userId, // Используем переданный или тестовый userId
                    lock = it.ad.lock
                )
            )
        }
    )
    private val settings = MkplCorSettings(
        repoTest = repo
    )
    private val processor = MkplAdProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adRequest = MkplAdvertisement(
                title = "Toyota Camry 2018 года",
                description = "Автомобиль в отличном состоянии",
                price = 1500000.0,
                car = MkplCar( // Предполагаю структуру MkplCar на основе спецификации
                    brand = "Toyota",
                    model = "Camry",
                    year = 2018,
                    mileage = 75000,
                    color = "Черный",
                    engine = MkplEngine(
                        type = MkplEngineType.GASOLINE,
                        volume = 2.5,
                        horsePower = 181,
                    ),
                    transmission = MkplTransmission.AUTOMATIC
                ),
                status = McplAdvertisementStatus.DRAFT,
                location = "Москва",
                contactPhone = "+79991234567",
                authorId = userId
            ),
        )

        processor.exec(ctx)

        assertEquals(MkplState.FINISHING, ctx.state)
        assertNotEquals(MkplAdvertisementId.NONE, ctx.adResponse.id)
        assertEquals("Toyota Camry 2018 года", ctx.adResponse.title)
        assertEquals("Автомобиль в отличном состоянии", ctx.adResponse.description)
        assertEquals(1500000.0, ctx.adResponse.price)
        assertEquals("Toyota", ctx.adResponse.car.brand)
        assertEquals("Camry", ctx.adResponse.car.model)
        assertEquals(2018, ctx.adResponse.car.year)
        assertEquals(McplAdvertisementStatus.DRAFT, ctx.adResponse.status)
        assertEquals("Москва", ctx.adResponse.location)
        assertEquals("+79991234567", ctx.adResponse.contactPhone)
        assertEquals(userId, ctx.adResponse.authorId)
    }


}