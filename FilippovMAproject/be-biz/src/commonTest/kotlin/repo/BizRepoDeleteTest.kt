package com.github.watching1981.biz.repo

import kotlinx.coroutines.test.runTest
import repo.repoNotFoundTest
import com.github.watching1981.backend.repo.tests.AdRepositoryMock

import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.models.*
import com.github.watching1981.common.repo.DbAdResponseErr
import com.github.watching1981.common.repo.DbAdResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue



class BizRepoDeleteTest {

    private val userId = MkplUserId(0)
    private val command = MkplCommand.DELETE

    private val initAd = MkplAdvertisement(
        id = MkplAdvertisementId(123),
        title = "Toyota Camry 2018 года",
        description = "Автомобиль в отличном состоянии",
        price = 1500000.0,
        car = MkplCar(
            brand = "Toyota",
            model = "Camry",
            year = 2018
        ),
        status = McplAdvertisementStatus.ACTIVE,
        location = "Москва",
        contactPhone = "+79991234567",
        authorId = userId,
        lock = MkplAdLock("lock-123")
    )

    private val repo = AdRepositoryMock(
        invokeReadAd = {
            DbAdResponseOk(
                data = initAd
            )
        },
        invokeDeleteAd = {
            if (it.id == initAd.id)
                DbAdResponseOk(
                    data = initAd
                )
            else DbAdResponseErr()
        }
    )


    private val settings by lazy {
        MkplCorSettings(
            repoTest = repo
        )
    }

    private val processor = MkplAdProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val adToDelete = MkplAdvertisement(
            id = MkplAdvertisementId(123),
            lock = MkplAdLock("lock-123")
        )

        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adRequest = adToDelete,
        )

        processor.exec(ctx)

        assertEquals(MkplState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
        assertEquals(initAd.price, ctx.adResponse.price)
        assertEquals(initAd.car.brand, ctx.adResponse.car.brand)
        assertEquals(initAd.car.model, ctx.adResponse.car.model)
        assertEquals(initAd.status, ctx.adResponse.status)
        assertEquals(initAd.location, ctx.adResponse.location)
        assertEquals(initAd.contactPhone, ctx.adResponse.contactPhone)
        assertEquals(initAd.authorId, ctx.adResponse.authorId)
    }

        @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)

}