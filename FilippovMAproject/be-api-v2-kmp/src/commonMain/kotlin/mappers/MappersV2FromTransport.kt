package com.github.watching1981.api.v2.mappers

import com.github.watching1981.api.v2.models.*

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import com.github.watching1981.common.models.MkplWorkMode
import com.github.watching1981.common.stubs.MkplStubs

fun MkplContext.fromTransport(request: IRequest) = when (request) {
    is AdCreateRequest -> fromTransport(request)
    is AdReadRequest -> fromTransport(request)
    is AdUpdateRequest -> fromTransport(request)
    is AdDeleteRequest -> fromTransport(request)
    is AdSearchRequest -> fromTransport(request)
    is AdOffersRequest -> fromTransport(request)
}

private fun String?.toAdId() = this?.let { MkplAdId(it) } ?: MkplAdId.NONE
private fun String?.toAdLock() = this?.let { MkplAdLock(it) } ?: MkplAdLock.NONE
private fun AdReadObject?.toInternal() = if (this != null) {
    MkplAd(id = id.toAdId())
} else {
    MkplAd()
}


private fun AdDebug?.transportToWorkMode(): MkplWorkMode = when (this?.mode) {
    AdRequestDebugMode.PROD -> MkplWorkMode.PROD
    AdRequestDebugMode.TEST -> MkplWorkMode.TEST
    AdRequestDebugMode.STUB -> MkplWorkMode.STUB
    null -> MkplWorkMode.PROD
}

private fun AdDebug?.transportToStubCase(): MkplStubs = when (this?.stub) {
    AdRequestDebugStubs.SUCCESS -> MkplStubs.SUCCESS
    AdRequestDebugStubs.NOT_FOUND -> MkplStubs.NOT_FOUND
    AdRequestDebugStubs.BAD_ID -> MkplStubs.BAD_ID
    AdRequestDebugStubs.BAD_TITLE -> MkplStubs.BAD_TITLE
    AdRequestDebugStubs.BAD_DESCRIPTION -> MkplStubs.BAD_DESCRIPTION
    AdRequestDebugStubs.BAD_VISIBILITY -> MkplStubs.BAD_VISIBILITY
    AdRequestDebugStubs.CANNOT_DELETE -> MkplStubs.CANNOT_DELETE
    AdRequestDebugStubs.BAD_SEARCH_STRING -> MkplStubs.BAD_SEARCH_STRING
    null -> MkplStubs.NONE
}

fun MkplContext.fromTransport(request: AdCreateRequest) {
    command = MkplCommand.CREATE
    adRequest = request.ad?.toInternal() ?: MkplAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: AdReadRequest) {
    command = MkplCommand.READ
    adRequest = request.ad.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: AdUpdateRequest) {
    command = MkplCommand.UPDATE
    adRequest = request.ad?.toInternal() ?: MkplAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: AdDeleteRequest) {
    command = MkplCommand.DELETE
    adRequest = request.ad.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun AdDeleteObject?.toInternal(): MkplAd = if (this != null) {
    MkplAd(
        id = id.toAdId(),
        lock = lock.toAdLock(),
    )
} else {
    MkplAd()
}

fun MkplContext.fromTransport(request: AdSearchRequest) {
    command = MkplCommand.SEARCH
    adFilterRequest = request.adFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: AdOffersRequest) {
    command = MkplCommand.OFFERS
    adRequest = request.ad.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun AdSearchFilter?.toInternal(): MkplAdFilter = MkplAdFilter(
    searchString = this?.searchString ?: ""
)

private fun AdCreateObject.toInternal(): MkplAd = MkplAd(
    title = this.title ?: "",
    description = this.description ?: "",
    location = this.location ?: "",
    adType = this.adType.fromTransport(),
    status = this.status.fromTransport(),
)

private fun AdUpdateObject.toInternal(): MkplAd = MkplAd(
    id = this.id.toAdId(),
    title = this.title ?: "",
    description = this.description ?: "",
    location = this.location ?: "",
    adType = this.adType.fromTransport(),
    status = this.status.fromTransport(),
    lock = this.lock.toAdLock(),
)

private fun AdStatus?.fromTransport(): MkplStatus = when (this) {
    AdStatus.ACTIVE -> MkplStatus.ACTIVE
    AdStatus.DRAFT -> MkplStatus.DRAFT
    AdStatus.CLOSED -> MkplStatus.CLOSED
    AdStatus.ARCHIVED -> MkplStatus.ARCHIVED
    null -> MkplStatus.NONE
}

private fun DealSide?.fromTransport(): MkplDealSide = when (this) {
    DealSide.DEMAND -> MkplDealSide.DEMAND
    DealSide.SUPPLY -> MkplDealSide.SUPPLY
    null -> MkplDealSide.NONE
}
