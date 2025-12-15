
package com.github.watching1981.stubs

import com.github.watching1981.common.models.*
import kotlinx.datetime.Instant
object MkplAdStub {

    // Базовые заглушки для разных сценариев
    private val AD_ACTIVE_CAMRY = MkplAdvertisement(
        id = McplAdvertisementId(1),
        title = "Toyota Camry 2020",
        description = "Автомобиль в отличном состоянии, один хозяин",
        price = 1500000.0,
        car = MkplCar(
            brand = "Toyota",
            model = "Camry",
            year = 2020,
            mileage = 50000,
            color = "Черный",
            engine = MkplEngine(
                type = MkplEngineType.GASOLINE,
                volume = 2.5,
                horsePower = 200
            ),
            transmission = MkplTransmission.AUTOMATIC
        ),
        status = McplAdvertisementStatus.ACTIVE,
        location = "Москва",
        contactPhone = "+79991234567",
        authorId = MkplUserId(100),
        createdAt = Instant.parse("2023-10-01T10:00:00Z"),
        updatedAt = Instant.parse("2023-10-01T10:00:00Z"),
        viewsCount = 150,
        favoriteCount = 25
    )

    private val AD_DRAFT_BMW = MkplAdvertisement(
        id = McplAdvertisementId(2),
        title = "BMW X5 2019",
        description = "Премиальный SUV с полным комплектом",
        price = 2500000.0,
        car = MkplCar(
            brand = "BMW",
            model = "X5",
            year = 2019,
            mileage = 35000,
            color = "Белый",
            engine = MkplEngine(
                type = MkplEngineType.DIESEL,
                volume = 3.0,
                horsePower = 265
            ),
            transmission = MkplTransmission.AUTOMATIC
        ),
        status = McplAdvertisementStatus.DRAFT,
        location = "Санкт-Петербург",
        contactPhone = "+79997654321",
        authorId = MkplUserId(101),
        createdAt = Instant.parse("2023-10-02T11:00:00Z"),
        updatedAt = Instant.parse("2023-10-02T11:00:00Z"),
        viewsCount = 75,
        favoriteCount = 12
    )




    fun get(): MkplAdvertisement = AD_ACTIVE_CAMRY.copy()

    fun getDraft(): MkplAdvertisement = AD_DRAFT_BMW.copy()

    fun prepareResult(block: MkplAdvertisement.() -> Unit): MkplAdvertisement = get().apply(block)

    fun prepareResultDraft(block: MkplAdvertisement.() -> Unit): MkplAdvertisement = getDraft().apply(block)

    // Подготовка списков для поиска
    fun prepareSearchList(brand: String? = null, status: McplAdvertisementStatus? = null): List<MkplAdvertisement> {
        val baseList = listOf(
            AD_ACTIVE_CAMRY.copy(),
            AD_DRAFT_BMW.copy(),
            createAd("toyota-1", "Toyota Corolla 2020", "Toyota", "Corolla", 2020, 40000, 1100000.0),
            createAd("bmw-1", "BMW 3 Series 2021", "BMW", "3 Series", 2021, 15000, 2800000.0),
        )

        return baseList.filter { ad ->
            (brand == null || ad.car.brand.equals(brand, ignoreCase = true)) &&
                    (status == null || ad.status == status)
        }
    }

    fun prepareActiveAdsList(brand: String? = null): List<MkplAdvertisement> =
        prepareSearchList(brand, McplAdvertisementStatus.ACTIVE)

    fun prepareDraftAdsList(): List<MkplAdvertisement> =
        prepareSearchList(status = McplAdvertisementStatus.DRAFT)



    // Подготовка списков по цене
    fun prepareAdsByPriceRange(minPrice: Double, maxPrice: Double): List<MkplAdvertisement> {
        return prepareSearchList().filter { ad ->
            ad.price >= minPrice && ad.price <= maxPrice
        }
    }

    // Подготовка списков по году
    fun prepareAdsByYearRange(minYear: Int, maxYear: Int): List<MkplAdvertisement> {
        return prepareSearchList().filter { ad ->
            ad.car.year in minYear..maxYear
        }
    }

    // Создание кастомного объявления
    fun createCustomAd(
        id: String,
        title: String,
        brand: String,
        model: String,
        year: Int,
        mileage: Int,
        price: Double,
        status: McplAdvertisementStatus = McplAdvertisementStatus.ACTIVE,
        location: String = "Москва"
    ): MkplAdvertisement = AD_ACTIVE_CAMRY.copy(
        id = McplAdvertisementId(id.toLong()),
        title = title,
        description = "Описание для $title",
        price = price,
        car = AD_ACTIVE_CAMRY.car.copy(
            brand = brand,
            model = model,
            year = year,
            mileage = mileage
        ),
        status = status,
        location = location,

    )

    // Приватные методы
    private fun createAd(
        id: String,
        title: String,
        brand: String,
        model: String,
        year: Int,
        mileage: Int,
        price: Double
    ): MkplAdvertisement = AD_ACTIVE_CAMRY.copy(
        id = McplAdvertisementId(id.hashCode().toLong()),
        title = title,
        description = "Описание для $title",
        price = price,
        car = AD_ACTIVE_CAMRY.car.copy(
            brand = brand,
            model = model,
            year = year,
            mileage = mileage
        ),

    )
}