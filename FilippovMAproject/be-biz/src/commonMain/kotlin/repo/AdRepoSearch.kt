package com.github.watching1981.biz.repo

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.fail
import com.github.watching1981.common.models.MkplEngine
import com.github.watching1981.common.models.MkplEngineType
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.common.repo.DbAdFilterRequest
import com.github.watching1981.common.repo.DbAdsResponseErr
import com.github.watching1981.common.repo.DbAdsResponseOk
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker

fun ICorChainDsl<MkplContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == MkplState.RUNNING }
    handle {
        val filters = adFilterValidated.filters
        val request = DbAdFilterRequest(
            // Фильтры автомобиля
            titleFilter = filters.title ?: "",
            descriptionFilter = filters.description ?: "",
            brandFilter = filters.brand ?: "",
            modelFilter = filters.model ?: "",
            minYear = filters.minYear,
            maxYear = filters.maxYear,
            minPrice = filters.minPrice,
            maxPrice = filters.maxPrice,
            minMileage = filters.minMileage,
            maxMileage = filters.maxMileage,

        )
        when(val result = adRepo.searchAd(request)) {
            is DbAdsResponseOk -> adsRepoDone = result.data.toMutableList()
            is DbAdsResponseErr -> fail(result.errors)
        }

    }

}
