package com.github.watching1981.mappers.v1

import com.github.watching1981.api.v1.models.AdCreateResponse
import com.github.watching1981.api.v1.models.AdDeleteResponse
import com.github.watching1981.api.v1.models.AdOffersResponse
import com.github.watching1981.api.v1.models.AdPermissions
import com.github.watching1981.api.v1.models.AdReadResponse
import com.github.watching1981.api.v1.models.AdResponseObject
import com.github.watching1981.api.v1.models.AdSearchResponse
import com.github.watching1981.api.v1.models.AdUpdateResponse
import com.github.watching1981.api.v1.models.AdStatus
import com.github.watching1981.api.v1.models.DealSide
import com.github.watching1981.api.v1.models.Error
import com.github.watching1981.api.v1.models.IResponse
import com.github.watching1981.api.v1.models.ResponseResult
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.exceptions.UnknownMkplCommand
import com.github.watching1981.common.models.MkplAd
import com.github.watching1981.common.models.MkplAdId
import com.github.watching1981.common.models.MkplAdPermissionClient
import com.github.watching1981.common.models.MkplCommand
import com.github.watching1981.common.models.MkplDealSide
import com.github.watching1981.common.models.MkplError
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.common.models.MkplUserId
import com.github.watching1981.common.models.MkplStatus

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
    ad = adResponse.toTransportAd(),
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

fun MkplAd.toTransportAd(): AdResponseObject = AdResponseObject(
    id = id.toTransportAd(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != MkplUserId.NONE }?.asString(),
    adType = adType.toTransportAd(),
    status = status.toTransportAd(),
    permissions = permissionsClient.toTransportAd(),
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
    MkplState.RUNNING -> ResponseResult.SUCCESS
    MkplState.FAILING -> ResponseResult.ERROR
    MkplState.FINISHING -> ResponseResult.SUCCESS
    MkplState.NONE -> null
}
