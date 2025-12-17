package com.github.watching1981.api.log1.mapper
import kotlinx.datetime.Clock
import com.github.watching1981.api.log1.models.*
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import kotlinx.datetime.Instant


fun MkplContext.toLog(logId: String): CommonLogModel = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "car-marketplace-service",
    level = determineLogLevel(),
    ad = toMkplLog(),
    errors = errors.map { it.toLog() },
    durationMs = calculateDurationMs()
)

private fun MkplContext.determineLogLevel(): LogLevel = when {
    errors.isNotEmpty() -> LogLevel.ERROR
    state == MkplState.FAILING -> LogLevel.WARN
    else -> LogLevel.INFO
}

private fun MkplContext.calculateDurationMs(): Int? {
    return if (timeStart != Instant.DISTANT_FUTURE ) {
        (Clock.System.now().toEpochMilliseconds() - timeStart.toEpochMilliseconds()).toInt()
    } else {
        null
    }
}

private fun MkplContext.toMkplLog(): MkplAdLogModel? {
    val adNone = MkplAdvertisement()
    return MkplAdLogModel(
        requestId = requestId.takeIf { it != MkplRequestId.NONE }?.asString(),
        operation = command.toLogOperation(),
        requestAd = adRequest.takeIf { it != adNone }?.toLog(),
        requestFilter = adFilterRequest.takeIf { it != MkplAdvertisementSearch(MkplAdvertisementFilters.NONE,MkplPagination(),MkplSortOptions()) }?.toLog(),
        responseAd = adResponse.takeIf { it != adNone }?.toLog(),
        responseAds = adsResponse.takeIf { it.isNotEmpty() }?.filter { it != adNone }?.map { it.toLog() },
        responseTotal = adsResponse.size.takeIf { it > 0 }
    ).takeIf { it != MkplAdLogModel() }
}

private fun MkplCommand.toLogOperation(): MkplLogOperation? = when (this) {
    MkplCommand.CREATE -> MkplLogOperation.CREATE
    MkplCommand.GET -> MkplLogOperation.READ
    MkplCommand.UPDATE -> MkplLogOperation.UPDATE
    MkplCommand.DELETE -> MkplLogOperation.DELETE
    MkplCommand.SEARCH -> MkplLogOperation.SEARCH
    MkplCommand.NONE -> null
}

private fun MkplAdvertisementSearch.toLog(): AdFilterLog? {
    val filters = this.filters
    return AdFilterLog(
        brand = filters.brand.takeIf { it?.isNotBlank() == true },
        model = filters.model.takeIf { it?.isNotBlank() == true },
        minYear = filters.minYear,
        maxYear = filters.maxYear,
        minPrice = filters.minPrice?.toDouble(),
        maxPrice = filters.maxPrice?.toDouble(),
        page = pagination.page.takeIf { it > 0 },
        propertySize = pagination.size.takeIf { it != 20 } // 20 - default value
    ).takeIf {
        it.brand != null || it.model != null || it.minYear != null ||
                it.maxYear != null || it.minPrice != null || it.maxPrice != null ||
                it.page != null || it.propertySize != null
    }
}

private fun MkplError.toLog(): ErrorLogModel = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it?.isNotBlank() == true },
    code = code.takeIf { it.isNotBlank() }
)

private fun MkplAdvertisement.toLog(): AdLog = AdLog(
    id = id.takeIf { it != McplAdvertisementId.NONE }?.asLong(),
    title = title.takeIf { it.isNotBlank() },
    status = status.toLogStatus(),
    price = price.takeIf { it > 0.0 }?.toDouble(),
    ownerId = authorId.takeIf { it != MkplUserId.NONE }?.value,
    carBrand = car.brand.takeIf { it.isNotBlank() },
    carModel = car.model.takeIf { it.isNotBlank() }
)

private fun McplAdvertisementStatus.toLogStatus(): AdStatus? = when (this) {
    McplAdvertisementStatus.DRAFT -> AdStatus.DRAFT
    McplAdvertisementStatus.ACTIVE -> AdStatus.ACTIVE
    McplAdvertisementStatus.SOLD -> AdStatus.SOLD
    McplAdvertisementStatus.ARCHIVED -> AdStatus.ARCHIVED
    McplAdvertisementStatus.NONE -> null
}