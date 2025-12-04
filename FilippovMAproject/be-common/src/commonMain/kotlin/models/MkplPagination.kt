package com.github.watching1981.common.models

data class MkplPagination(
    val page: Int=0,
    val size: Int=20
) {
    init {
        require(page >= 0) { "Page must be non-negative" }
        require(size in 1..100) { "Size must be between 1 and 100" }
    }

    val offset: Int get() = page * size
}