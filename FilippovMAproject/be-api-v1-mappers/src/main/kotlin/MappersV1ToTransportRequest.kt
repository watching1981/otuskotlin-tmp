package com.github.watching1981.mappers.v1

import com.github.watching1981.api.v1.models.AdCreateObject
import com.github.watching1981.api.v1.models.AdDeleteObject
import com.github.watching1981.api.v1.models.AdReadObject
import com.github.watching1981.api.v1.models.AdUpdateObject
import com.github.watching1981.common.models.MkplAd
import com.github.watching1981.common.models.MkplAdLock

fun MkplAd.toTransportCreateAd() = AdCreateObject(
    title = title,
    description = description,
    adType = adType.toTransportAd(),
    status = status.toTransportAd(),
)

fun MkplAd.toTransportReadAd() = AdReadObject(
    id = id.toTransportAd()
)

fun MkplAd.toTransportUpdateAd() = AdUpdateObject(
    id = id.toTransportAd(),
    title = title,
    description = description,
    adType = adType.toTransportAd(),
    status = status.toTransportAd(),
    lock = lock.toTransportAd(),
)

internal fun MkplAdLock.toTransportAd() = takeIf { it != MkplAdLock.NONE }?.asString()

fun MkplAd.toTransportDeleteAd() = AdDeleteObject(
    id = id.toTransportAd(),
    lock = lock.toTransportAd(),
)