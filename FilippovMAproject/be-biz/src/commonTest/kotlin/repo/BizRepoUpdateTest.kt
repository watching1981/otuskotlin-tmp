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


class BizRepoUpdateTest {

    private val userId = MkplUserId(0)
    private val command = MkplCommand.UPDATE

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
            ),

            transmission = MkplTransmission.AUTOMATIC
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
                data = initAd
            )
        },
        invokeUpdateAd = { request ->
            DbAdResponseOk(
                data = MkplAdvertisement(
                    id = request.ad.id,
                    title = request.ad.title,
                    description = request.ad.description,
                    price = request.ad.price,
                    car = request.ad.car,
                    status = request.ad.status ?: initAd.status,
                    location = request.ad.location ?: initAd.location,
                    contactPhone = request.ad.contactPhone ?: initAd.contactPhone,
                    authorId = request.ad.authorId ?: initAd.authorId,
                    lock = MkplAdLock("new-lock-123"), // Новая версия блокировки
                    viewsCount = initAd.viewsCount,
                    favoriteCount = initAd.favoriteCount,

                )
            )
        }
    )

    private val settings = MkplCorSettings(repoTest = repo)
    private val processor = MkplAdProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val adToUpdate = MkplAdvertisement(
            id = MkplAdvertisementId(123),
            title = "Toyota Camry 2018 года (обновленный)",
            description = "Автомобиль в отличном состоянии, пробег 75 000 км",
            price = 1400000.0, // Снизили цену
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
                ),

                transmission = MkplTransmission.AUTOMATIC
            ),
            status = McplAdvertisementStatus.ACTIVE,
            location = "Москва",
            lock = MkplAdLock("lock-123") // Текущая версия блокировки
        )

        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adRequest = adToUpdate,
        )

        processor.exec(ctx)

        assertEquals(MkplState.FINISHING, ctx.state)

        assertEquals(adToUpdate.id, ctx.adResponse.id)
        assertEquals(adToUpdate.title, ctx.adResponse.title)
        assertEquals(adToUpdate.description, ctx.adResponse.description)
        assertEquals(adToUpdate.price, ctx.adResponse.price)
        assertEquals(adToUpdate.car.brand, ctx.adResponse.car.brand)
        assertEquals(adToUpdate.car.model, ctx.adResponse.car.model)
        assertEquals(adToUpdate.car.year, ctx.adResponse.car.year)
        assertEquals(adToUpdate.car.mileage, ctx.adResponse.car.mileage)
        assertEquals(adToUpdate.car.engine.type, ctx.adResponse.car.engine.type)
        assertEquals(adToUpdate.car.engine.volume, ctx.adResponse.car.engine.volume)
        assertEquals(adToUpdate.car.engine.horsePower, ctx.adResponse.car.engine.horsePower)
        assertEquals(adToUpdate.car.transmission, ctx.adResponse.car.transmission)
        assertEquals(adToUpdate.status, ctx.adResponse.status)
        assertEquals(userId, ctx.adResponse.authorId)



    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
