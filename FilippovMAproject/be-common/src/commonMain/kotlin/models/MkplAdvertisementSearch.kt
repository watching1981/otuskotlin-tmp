package com.github.watching1981.common.models

data class MkplAdvertisementSearch(
    val filters: MkplAdvertisementFilters,
    val pagination: MkplPagination,
    val sort: MkplSortOptions
)