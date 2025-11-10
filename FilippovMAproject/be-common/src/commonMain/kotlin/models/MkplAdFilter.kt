package com.github.watching1981.common.models

data class MkplAdFilter(
    var searchString: String = "",
    var ownerId: MkplUserId = MkplUserId.NONE,
    var dealSide: MkplDealSide = MkplDealSide.NONE,
)
