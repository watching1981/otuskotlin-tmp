package com.github.watching1981.common

import kotlinx.datetime.Instant
import com.github.watching1981.common.models.*


data class MkplContext(
    var command: MkplCommand = MkplCommand.NONE,
    var state: MkplState = MkplState.NONE,
    val errors: MutableList<MkplError> = mutableListOf(),


    var requestId: MkplRequestId = MkplRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var adRequest: MkplAdvertisement = MkplAdvertisement(),
    var adFilterRequest: MkplAdvertisementSearch = MkplAdvertisementSearch(MkplAdvertisementFilters.NONE,MkplPagination(),MkplSortOptions()),
    var adResponse: MkplAdvertisement = MkplAdvertisement(),
    var adsResponse: MutableList<MkplAdvertisement> = mutableListOf(),
)