package com.github.watching1981.common.repo

import com.github.watching1981.common.models.MkplAdvertisement
import com.github.watching1981.common.models.MkplError

sealed interface IDbAdsResponse: IDbResponse<List<MkplAdvertisement>>

data class DbAdsResponseOk(
    val data: List<MkplAdvertisement>
): IDbAdsResponse

@Suppress("unused")
data class DbAdsResponseErr(
    val errors: List<MkplError> = emptyList()
): IDbAdsResponse {
    constructor(err: MkplError): this(listOf(err))
}
