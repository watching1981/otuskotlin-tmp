package com.github.watching1981.e2e.be.scenarios.v1.base

import com.github.watching1981.api.v1.models.*

val someCreateAd = AdCreateRequest(
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
        horsePower = 120,
        transmission = Transmission.AUTOMATIC
    ),
    location = "Москва",
    contactPhone = "+79991234567"
)
