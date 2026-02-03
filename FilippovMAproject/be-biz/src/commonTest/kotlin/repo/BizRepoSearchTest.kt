package com.github.watching1981.biz.repo

import kotlinx.coroutines.test.runTest
import com.github.watching1981.backend.repo.tests.AdRepositoryMock
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.models.*
import com.github.watching1981.common.repo.DbAdsResponseOk
import com.github.watching1981.common.repo.SortDirection
import kotlin.test.Test
import kotlin.test.assertEquals


class BizRepoSearchTest {

    private val userId1 = MkplUserId(321)
    private val userId2 = MkplUserId(654)
    private val command = MkplCommand.SEARCH

    private val initAds = listOf(
        MkplAdvertisement(
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
            authorId = userId1,
            lock = MkplAdLock("lock-123"),
            viewsCount = 100,
            favoriteCount = 25
        ),
        MkplAdvertisement(
            id = MkplAdvertisementId(124),
            title = "Honda Civic 2020 года",
            description = "Экономичный автомобиль, низкий расход",
            price = 1200000.0,
            car = MkplCar(
                brand = "Honda",
                model = "Civic",
                year = 2020,
                mileage = 40000,
                color = "Белый",
                MkplEngine(
                    type = MkplEngineType.GASOLINE,
                    volume = 1.6,
                    horsePower = 125,
                ),

                        transmission = MkplTransmission.MANUAL
            ),
            status = McplAdvertisementStatus.ACTIVE,
            location = "Санкт-Петербург",
            contactPhone = "+79998765432",
            authorId = userId2,
            lock = MkplAdLock("lock-124"),
            viewsCount = 50,
            favoriteCount = 15
        ),
        MkplAdvertisement(
            id = MkplAdvertisementId(125),
            title = "Tesla Model 3 2022 года",
            description = "Электромобиль, полный привод",
            price = 3500000.0,
            car = MkplCar(
                brand = "Tesla",
                model = "Model 3",
                year = 2022,
                mileage = 20000,
                color = "Красный",
                MkplEngine(
                    type = MkplEngineType.ELECTRIC,
                    volume = 0.0,
                    horsePower = 450,
                ),

                transmission = MkplTransmission.AUTOMATIC
            ),
            status = McplAdvertisementStatus.ACTIVE,
            location = "Москва",
            contactPhone = "+79991112233",
            authorId = userId1,
            lock = MkplAdLock("lock-125"),
            viewsCount = 200,
            favoriteCount = 40
        )
    )

    private val repo = AdRepositoryMock(
        invokeSearchAd = { request ->
            // Фильтрация по параметрам
            var filteredAds = initAds

            // Фильтрация по бренду
            if (request.brandFilter.isNotBlank()) {
                filteredAds = filteredAds.filter {
                    it.car.brand.contains(request.brandFilter, ignoreCase = true)
                }
            }

            // Фильтрация по модели
            if (request.modelFilter.isNotBlank()) {
                filteredAds = filteredAds.filter {
                    it.car.model.contains(request.modelFilter, ignoreCase = true)
                }
            }

            DbAdsResponseOk(
                data = filteredAds,
            )
        }
    )

    private val settings = MkplCorSettings(repoTest = repo)
    private val processor = MkplAdProcessor(settings)

    @Test
    fun repoSearchByBrandAndLocationTest() = runTest {
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adFilterRequest = MkplAdvertisementSearch(
                filters = MkplAdvertisementFilters(
                    brand = "Toyota",
                    location = "Москва"
                ),
                pagination = MkplPagination(
                    page = 0,
                    size = 10
                ),
                sort = MkplSortOptions(
                    MkplSortField.PRICE,
                    MkplSortDirection.ASC
                )
            ),
        )

        processor.exec(ctx)

        assertEquals(MkplState.FINISHING, ctx.state)

        assertEquals(1, ctx.adsResponse.size) // Должен найти только Toyota Camry в Москве
        assertEquals("Toyota", ctx.adsResponse[0].car.brand)
        assertEquals("Camry", ctx.adsResponse[0].car.model)
        assertEquals("Москва", ctx.adsResponse[0].location)
    }
}
