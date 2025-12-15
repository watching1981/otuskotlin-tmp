package com.github.watching1981.mappers.v1

import com.github.watching1981.api.v1.models.*
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*


import com.github.watching1981.mappers.v1.exceptions.UnknownRequestClass
import kotlinx.datetime.Clock

fun MkplContext.fromTransport(request: IRequest) = when (request) {
    is AdCreateRequest -> fromTransportCreate(request)
    is AdGetRequest -> fromTransportGet(request)
    is AdUpdateRequest -> fromTransportUpdate(request)
    is AdDeleteRequest -> fromTransportDelete(request)
    is AdSearchRequest -> fromTransportSearch(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun Long?.toAdId() = this?.let { McplAdvertisementId(it) } ?: McplAdvertisementId.NONE
private fun String?.toAdLock() = this?.let { MkplAdLock(it) } ?: MkplAdLock.NONE


fun MkplContext.fromTransportGet(request: AdGetRequest) {
    command = MkplCommand.GET
    adRequest = request.toBusiness()
}

private fun AdGetRequest?.toBusiness(): MkplAdvertisement = if (this != null) {
    MkplAdvertisement(id = id.toAdId())
} else {
    MkplAdvertisement()
}



fun MkplContext.fromTransportCreate(request: AdCreateRequest) {
    command = MkplCommand.CREATE
    adRequest = request.toBusiness()
}
fun CarInfo.fromTransport():MkplCar= MkplCar(
    brand = this.brand,
    model = this.model,
    year = this.year,
    mileage = this.mileage ?: 0,
    color = this.color ?: "",
    engine = MkplEngine(
        type = this.engineType?.fromTransport() ?:MkplEngineType.GASOLINE ,
        volume = this.engineVolume ?: 0.0,
        horsePower = this.horsePower
    ),
    transmission = this.transmission?.fromTransport()?:MkplTransmission.MANUAL
)
fun EngineType.fromTransport():MkplEngineType= when(this) {
    EngineType.GASOLINE -> MkplEngineType.GASOLINE
    EngineType.DIESEL -> MkplEngineType.DIESEL
    EngineType.HYBRID -> MkplEngineType.HYBRID
    EngineType.ELECTRIC->MkplEngineType.ELECTRIC
}
fun Transmission.fromTransport():MkplTransmission=when(this) {
    Transmission.AUTOMATIC ->MkplTransmission.AUTOMATIC
    Transmission.MANUAL ->MkplTransmission.MANUAL
    Transmission.ROBOT ->MkplTransmission.ROBOT
    Transmission.VARIATOR ->MkplTransmission.VARIATOR
}

private fun AdCreateRequest.toBusiness(): MkplAdvertisement = MkplAdvertisement(
    id = McplAdvertisementId.NONE,
    title = this.title,
    description = this.description,
    price = this.price,
    car = this.carInfo.fromTransport(),
    status = McplAdvertisementStatus.DRAFT,
    location = this.location ?: "",
    contactPhone = this.contactPhone ?: "",
    createdAt = Clock.System.now(),
    updatedAt = Clock.System.now(),
)


fun MkplContext.fromTransportUpdate(request: AdUpdateRequest) {
    command = MkplCommand.UPDATE
    adRequest = request.toBusiness()
}
fun AdUpdateRequest.toBusiness(): MkplAdvertisement = MkplAdvertisement(
    id = this.id.toAdId(),
    title = this.title ?: "",
    description = this.description ?: "",
    price = this.price?: 0.0,
    status = this.status.fromTransport(),
    car = this.carInfo?.fromTransport() ?: MkplCar.NONE,
    updatedAt = Clock.System.now(),
    lock = lock.toAdLock(),
)

fun MkplContext.fromTransportDelete(request: AdDeleteRequest) {
    command = MkplCommand.DELETE
    adRequest = request.toBusiness()
}

private fun AdDeleteRequest.toBusiness(): MkplAdvertisement = if (this != null) {
    MkplAdvertisement(
        id = id.toAdId(),
        lock = lock.toAdLock(),
    )
} else {
    MkplAdvertisement()
}

fun MkplContext.fromTransportSearch(request: AdSearchRequest) {
    command = MkplCommand.SEARCH
    adFilterRequest = request.toBusiness()

}
fun AdSearchRequest.toBusiness(): MkplAdvertisementSearch = MkplAdvertisementSearch(
    filters = MkplAdvertisementFilters(
        brand = this.brand,
        model = this.model,
        minYear = this.minYear,
        maxYear = this.maxYear,
        minPrice = this.minPrice,
        maxPrice = this.maxPrice,
        location = this.location
    ),
    pagination = MkplPagination(
        page = this.page ?: 0,
        size = this.propertySize ?: 20
    ),
    sort = MkplSortOptions(
        field = MkplSortField.CREATED_AT, // По умолчанию
        direction = MkplSortDirection.DESC
    )
)



private fun AdStatus?.fromTransport(): McplAdvertisementStatus = when (this) {
    AdStatus.ACTIVE -> McplAdvertisementStatus.ACTIVE
    AdStatus.DRAFT -> McplAdvertisementStatus.DRAFT
    AdStatus.SOLD -> McplAdvertisementStatus.SOLD
    AdStatus.ARCHIVED -> McplAdvertisementStatus.ARCHIVED
    null -> McplAdvertisementStatus.NONE
}
//


