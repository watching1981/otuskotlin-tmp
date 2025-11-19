package com.github.watching1981.api.v2.mappers


import com.github.watching1981.api.v2.models.*
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import kotlinx.datetime.Clock


fun MkplContext.toTransportAd(): Any = when (this.command) {
    MkplCommand.CREATE -> this.toAdCreateResponse()
    MkplCommand.GET -> this.toAdGetResponse()  // GET -> READ
    MkplCommand.UPDATE -> this.toAdUpdateResponse()
    MkplCommand.DELETE -> this.toAdDeleteResponse()
    MkplCommand.SEARCH -> this.toAdSearchResponse()
    MkplCommand.NONE -> throw IllegalStateException("No command specified in context")
}


private fun Long?.toAdId() = this?.let { McplAdvertisementId(it) } ?: McplAdvertisementId.NONE

fun MkplContext.toAdCreateResponse(): AdCreateResponse =
    AdCreateResponse(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        timestamp = Clock.System.now().toString(),
        ad = this.adResponse.toAdData(),
        adId = this.adResponse.id.toTransportAd()
    )
fun MkplContext.toAdGetResponse(): AdGetResponse =
    AdGetResponse(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        timestamp = Clock.System.now().toString(),
        ad = this.adResponse.toAdData()
    )
fun MkplContext.toAdUpdateResponse(): AdUpdateResponse =
    AdUpdateResponse(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        timestamp = Clock.System.now().toString(),
        ad = this.adResponse.toAdData(),
        previousVersion = null
    )
fun MkplContext.toAdDeleteResponse(): AdDeleteResponse =
    AdDeleteResponse(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        timestamp = Clock.System.now().toString(),
        deletedAdId = this.adResponse.id.toTransportAd(),
        deletionTime = Clock.System.now().toString(),
    )
fun MkplContext.toAdSearchResponse(): AdSearchResponse =
    AdSearchResponse(
        result = state.toResult(),
        errors = errors.toTransportErrors(),
        timestamp = Clock.System.now().toString(),
        ads = this.adsResponse.map { it.toAdData() },
        total = this.adsResponse.size,
        page = this.adFilterRequest.pagination.page,
        propertySize = this.adFilterRequest.pagination.size,
        totalPages = calculateTotalPages(this.adsResponse.size, this.adFilterRequest.pagination.size),
        hasNext = false,
        hasPrevious = false,
        searchId = null
    )
private fun calculateTotalPages(total: Int, size: Int): Int =
    if (total == 0) 0 else (total + size - 1) / size


private fun MkplAdvertisement.toAdData(): AdData = AdData(
    id = this.id.toTransportAd(),
    title = this.title,
    description = this.description,
    price = this.price.toDouble(),
    carInfo = this.car.toCarInfo(),
    status = this.status.toTransport(),
    location = this.location,
    contactPhone = this.contactPhone,
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt.toString(),
    viewsCount = this.viewsCount,
    favoriteCount = this.favoriteCount
)
private fun MkplCar.toCarInfo(): CarInfo = CarInfo(
    brand = this.brand,
    model = this.model,
    year = this.year,
    mileage = this.mileage,
    color = this.color,
    engineType = this.engine.type.toTransport(),
    engineVolume = this.engine.volume,
    horsePower = this.engine.horsePower,
    transmission = this.transmission.toTransport()
)
private fun MkplEngineType.toTransport():EngineType= when (this) {
    MkplEngineType.HYBRID -> EngineType.HYBRID
    MkplEngineType.DIESEL -> EngineType.DIESEL
    MkplEngineType.ELECTRIC -> EngineType.ELECTRIC
    MkplEngineType.GASOLINE -> EngineType.GASOLINE
}
private fun MkplTransmission.toTransport():Transmission= when (this) {
    MkplTransmission.ROBOT -> Transmission.ROBOT
    MkplTransmission.MANUAL -> Transmission.MANUAL
    MkplTransmission.VARIATOR -> Transmission.VARIATOR
    MkplTransmission.AUTOMATIC -> Transmission.AUTOMATIC
}
internal fun McplAdvertisementStatus.toTransport(): AdStatus? = when (this) {
    McplAdvertisementStatus.ACTIVE->  AdStatus.ACTIVE
    McplAdvertisementStatus.DRAFT -> AdStatus.DRAFT
    McplAdvertisementStatus.SOLD -> AdStatus.SOLD
    McplAdvertisementStatus.ARCHIVED -> AdStatus.ARCHIVED
    McplAdvertisementStatus.NONE -> null
}

internal fun McplAdvertisementId.toTransportAd() = takeIf { it != McplAdvertisementId.NONE }?.asString()
private fun List<MkplError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportAd() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MkplError.toTransportAd() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun MkplState.toResult(): ResponseResult? = when (this) {
    MkplState.RUNNING -> ResponseResult.SUCCESS
    MkplState.FAILING -> ResponseResult.ERROR
    MkplState.FINISHING -> ResponseResult.SUCCESS
    MkplState.NONE -> null
}



