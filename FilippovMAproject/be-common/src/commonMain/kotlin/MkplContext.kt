package com.github.watching1981.common

import kotlinx.datetime.Instant
import com.github.watching1981.common.models.*
import com.github.watching1981.common.ws.IMkplWsSession
import com.github.watching1981.common.stubs.MkplStubs
import com.github.watching1981.common.repo.IRepoAd



data class MkplContext(
    var command: MkplCommand = MkplCommand.NONE,
    var state: MkplState = MkplState.NONE,
    val errors: MutableList<MkplError> = mutableListOf(),

    var corSettings: MkplCorSettings = MkplCorSettings(),
    var workMode: MkplWorkMode = MkplWorkMode.PROD,
    var stubCase: MkplStubs = MkplStubs.NONE,
    var wsSession: IMkplWsSession = IMkplWsSession.NONE,

    var requestId: MkplRequestId = MkplRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var adRequest: MkplAdvertisement = MkplAdvertisement(),
    var adFilterRequest: MkplAdvertisementSearch = MkplAdvertisementSearch(MkplAdvertisementFilters.NONE,MkplPagination(),MkplSortOptions()),
    var deletedAdId: MkplAdvertisementId = MkplAdvertisementId.NONE,
    var deletionTime: Instant? = null,

    var adValidating: MkplAdvertisement = MkplAdvertisement(),
    var adFilterValidating: MkplAdvertisementSearch = MkplAdvertisementSearch(MkplAdvertisementFilters.NONE,MkplPagination(),MkplSortOptions()),

    var adValidated: MkplAdvertisement = MkplAdvertisement(),
    var adFilterValidated: MkplAdvertisementSearch = MkplAdvertisementSearch(MkplAdvertisementFilters.NONE,MkplPagination(),MkplSortOptions()),

    var adRepo: IRepoAd = IRepoAd.NONE,
    var adRepoRead: MkplAdvertisement = MkplAdvertisement(), // То, что прочитали из репозитория
    var adRepoPrepare: MkplAdvertisement = MkplAdvertisement(), // То, что готовим для сохранения в БД
    var adRepoDone: MkplAdvertisement = MkplAdvertisement(),  // Результат, полученный из БД
    var adsRepoDone: MutableList<MkplAdvertisement> = mutableListOf(),

    var adResponse: MkplAdvertisement = MkplAdvertisement(),
    var adsResponse: MutableList<MkplAdvertisement> = mutableListOf(),
)