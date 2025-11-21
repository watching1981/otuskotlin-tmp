package com.github.watching1981.common.models

data class MkplSortOptions(
    val field: MkplSortField=MkplSortField.PRICE,
    val direction: MkplSortDirection=MkplSortDirection.ASC
)