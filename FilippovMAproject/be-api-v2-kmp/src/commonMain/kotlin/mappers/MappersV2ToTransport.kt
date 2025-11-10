package com.github.watching1981.api.v2.mappers

import com.github.watching1981.common.exceptions.UnknownMkplCommand
import com.github.watching1981.api.v2.models.*
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*

fun MkplContext.toTransportAd(): IResponse = when (val cmd = command) {
    MkplCommand.CREATE -> toTransportCreate()
    MkplCommand.READ -> toTransportRead()
    MkplCommand.UPDATE -> toTransportUpdate()
    MkplCommand.DELETE -> toTransportDelete()
    MkplCommand.SEARCH -> toTransportSearch()
    MkplCommand.OFFERS -> toTransportOffers()
    MkplCommand.NONE -> throw UnknownMkplCommand(cmd)
}

fun MkplContext.toTransportCreate() = AdCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun MkplContext.toTransportRead() = AdReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun MkplContext.toTransportUpdate() = AdUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun MkplContext.toTransportDelete() = AdDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun MkplContext.toTransportSearch() = AdSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ads = adsResponse.toTransportAd()
)

fun MkplContext.toTransportOffers() = AdOffersResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    ads = adsResponse.toTransportAd()
)

fun List<MkplAd>.toTransportAd(): List<AdResponseObject>? = this
    .map { it.toTransportAd() }
    .toList()
    .takeIf { it.isNotEmpty() }

internal fun MkplAd.toTransportAd(): AdResponseObject = AdResponseObject(
    id = id.toTransportAd(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    location = location.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != MkplUserId.NONE }?.asString(),
    adType = adType.toTransportAd(),
    status = status.toTransportAd(),
    permissions = permissionsClient.toTransportAd(),
//    productId = productId.takeIf { it != MkplProductId.NONE }?.asString(),
    lock = lock.takeIf { it != MkplAdLock.NONE }?.asString()
)

internal fun MkplAdId.toTransportAd() = takeIf { it != MkplAdId.NONE }?.asString()

private fun Set<MkplAdPermissionClient>.toTransportAd(): Set<AdPermissions>? = this
    .map { it.toTransportAd() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun MkplAdPermissionClient.toTransportAd() = when (this) {
    MkplAdPermissionClient.READ -> AdPermissions.READ
    MkplAdPermissionClient.UPDATE -> AdPermissions.UPDATE
    MkplAdPermissionClient.MAKE_VISIBLE_OWNER -> AdPermissions.MAKE_VISIBLE_OWN
    MkplAdPermissionClient.MAKE_VISIBLE_PUBLIC -> AdPermissions.MAKE_VISIBLE_PUBLIC
    MkplAdPermissionClient.DELETE -> AdPermissions.DELETE
}

internal fun MkplStatus.toTransportAd(): AdStatus? = when (this) {
    MkplStatus.ACTIVE->  AdStatus.ACTIVE
    MkplStatus.DRAFT -> AdStatus.DRAFT
    MkplStatus.CLOSED -> AdStatus.CLOSED
    MkplStatus.ARCHIVED -> AdStatus.ARCHIVED
    MkplStatus.NONE -> null
}



internal fun MkplDealSide.toTransportAd(): DealSide? = when (this) {
    MkplDealSide.DEMAND -> DealSide.DEMAND
    MkplDealSide.SUPPLY -> DealSide.SUPPLY
    MkplDealSide.NONE -> null
}

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
    MkplState.RUNNING, MkplState.FINISHING -> ResponseResult.SUCCESS
    MkplState.FAILING -> ResponseResult.ERROR
    MkplState.NONE -> null
}
