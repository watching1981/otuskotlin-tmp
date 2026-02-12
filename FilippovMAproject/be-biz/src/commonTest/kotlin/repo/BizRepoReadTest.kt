package com.github.watching1981.biz.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import com.github.watching1981.backend.repo.tests.AdRepositoryMock
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.models.*
import com.github.watching1981.common.repo.DbAdResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals


class BizRepoReadTest {

    private val userId = MkplUserId(0)
    private val command = MkplCommand.GET

    private val initAd = MkplAdvertisement(
        id = MkplAdvertisementId(123),
        title = "Toyota Camry 2018 года",
        description = "Автомобиль в отличном состоянии",
        price = 1500000.0,
        car = MkplCar(
            brand = "Toyota",
            model = "Camry",
            year = 2018,
            mileage = 75000,
            color = "Черный",
            MkplEngine(
                type = MkplEngineType.GASOLINE,
                volume = 2.5,
                horsePower = 181,
            )
        ),
        status = McplAdvertisementStatus.ACTIVE,
        location = "Москва",
        contactPhone = "+79991234567",
        authorId = userId,
        lock = MkplAdLock("lock-123"),
        viewsCount = 100,
        favoriteCount = 25
    )

    private val repo = AdRepositoryMock(
        invokeReadAd = {
            DbAdResponseOk(
                data = initAd.copy(
                    viewsCount = initAd.viewsCount + 1 // Увеличиваем счетчик просмотров
                )
            )
        }
    )

    private val settings = MkplCorSettings(repoTest = repo)
    private val processor = MkplAdProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adRequest = MkplAdvertisement(
                id = MkplAdvertisementId(123)
            ),
        )

        processor.exec(ctx)

        assertEquals(MkplState.FINISHING, ctx.state)
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
        assertEquals(initAd.price, ctx.adResponse.price)
        assertEquals(initAd.car.brand, ctx.adResponse.car.brand)
        assertEquals(initAd.car.model, ctx.adResponse.car.model)
        assertEquals(initAd.car.year, ctx.adResponse.car.year)
        assertEquals(initAd.car.mileage, ctx.adResponse.car.mileage)
        assertEquals(initAd.car.color, ctx.adResponse.car.color)
        assertEquals(initAd.car.engine.type, ctx.adResponse.car.engine.type)
        assertEquals(initAd.car.engine.volume, ctx.adResponse.car.engine.volume)
        assertEquals(initAd.car.engine.horsePower, ctx.adResponse.car.engine.horsePower)
        assertEquals(initAd.car.transmission, ctx.adResponse.car.transmission)
        assertEquals(initAd.status, ctx.adResponse.status)
        assertEquals(initAd.location, ctx.adResponse.location)
        assertEquals(initAd.contactPhone, ctx.adResponse.contactPhone)
        assertEquals(initAd.authorId, ctx.adResponse.authorId)
        assertEquals(initAd.lock, ctx.adResponse.lock)
        assertEquals(initAd.viewsCount + 1, ctx.adResponse.viewsCount) // Проверяем увеличение счетчика
        assertEquals(initAd.favoriteCount, ctx.adResponse.favoriteCount)
    }
    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)


}