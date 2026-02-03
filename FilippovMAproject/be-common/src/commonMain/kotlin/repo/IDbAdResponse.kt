package com.github.watching1981.common.repo

import com.github.watching1981.common.models.MkplAdvertisement
import com.github.watching1981.common.models.MkplError

sealed interface IDbAdResponse: IDbResponse<MkplAdvertisement>

data class DbAdResponseOk(
    val data: MkplAdvertisement
): IDbAdResponse

data class DbAdResponseErr(
    val errors: List<MkplError> = emptyList()
): IDbAdResponse {
    constructor(err: MkplError): this(listOf(err))
}

data class DbAdResponseErrWithData(
    val data: MkplAdvertisement,
    val errors: List<MkplError> = emptyList()
): IDbAdResponse {
    constructor(ad: MkplAdvertisement, err: MkplError): this(ad, listOf(err))
}
