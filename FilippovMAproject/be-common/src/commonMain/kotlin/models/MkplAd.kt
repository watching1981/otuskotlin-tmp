package com.github.watching1981.common.models

data class MkplAd(
    var id: MkplAdId = MkplAdId.NONE,
    var title: String = "",
    var description: String = "",
    var location: String ="",
    var ownerId: MkplUserId = MkplUserId.NONE,
    var adType: MkplDealSide = MkplDealSide.NONE,
    var status: MkplStatus = MkplStatus.NONE,
    var lock: MkplAdLock = MkplAdLock.NONE,
    val permissionsClient: MutableSet<MkplAdPermissionClient> = mutableSetOf(),
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = MkplAd()
    }
}
