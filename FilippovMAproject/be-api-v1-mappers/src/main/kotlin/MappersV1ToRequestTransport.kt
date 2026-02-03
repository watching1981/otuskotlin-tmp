package com.github.watching1981.mappers.v1

import com.github.watching1981.api.v1.models.*
import com.github.watching1981.common.models.*

fun MkplAdvertisement.toAdCreateRequest(): AdCreateRequest = AdCreateRequest(
    requestType = "create",
    title = this.title.takeIf { it.isNotBlank() } ?: "",
    description = this.description.takeIf { it.isNotBlank() } ?: "",
    price = this.price,
    carInfo = this.car.takeIf { it != MkplCar.NONE }?.toCarInfo() ?: CarInfo(
        brand = "",
        model = "",
        year = 0
    ),
    location = this.location.takeIf { it.isNotBlank() },
    contactPhone = this.contactPhone.takeIf { it.isNotBlank() }
)

fun MkplAdvertisement.toAdGetRequest(): AdGetRequest = AdGetRequest(
    requestType = "get",
    id = this.id.takeIf { it != MkplAdvertisementId.NONE }?.asLong() ?: 0
)


fun MkplAdvertisement.toAdUpdateRequest(): AdUpdateRequest = AdUpdateRequest(
    requestType = "update",
    id = this.id.takeIf { it != MkplAdvertisementId.NONE }?.asLong() ?: 0,
    lock = this.lock.takeIf { it != MkplAdLock.NONE }?.asString(),
    title = this.title.takeIf { it.isNotBlank() },
    description = this.description.takeIf { it.isNotBlank() },
    price = this.price.takeIf { it > 0 },
    carInfo = this.car.takeIf { it != MkplCar.NONE }?.toCarInfo(),
    status = this.status.takeIf { it != McplAdvertisementStatus.NONE }?.toTransport(),
    location = this.location.takeIf { it.isNotBlank() },
    contactPhone = this.contactPhone.takeIf { it.isNotBlank() }
)

fun MkplAdvertisement.toAdDeleteRequest(): AdDeleteRequest = AdDeleteRequest(
    requestType = "delete",
    id = this.id.takeIf { it != MkplAdvertisementId.NONE }?.asLong() ?: 0,
    lock = this.lock.takeIf { it != MkplAdLock.NONE }?.asString()
)



