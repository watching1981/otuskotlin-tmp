package com.github.watching1981.common.models
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant



data class MkplAdvertisement(
    var id: McplAdvertisementId =McplAdvertisementId.NONE,
    var title: String = "",
    var description: String ="",
    var price: Double = 0.0,
    var car: MkplCar = MkplCar.NONE,

    var status: McplAdvertisementStatus=McplAdvertisementStatus.DRAFT,
    var location: String ="",
    var contactPhone: String="",
    var authorId: MkplUserId=MkplUserId.NONE,
    var lock: MkplAdLock = MkplAdLock.NONE,
    val createdAt: Instant = Clock.System.now(),
    var updatedAt: Instant = Clock.System.now(),
    var viewsCount: Int = 0,
    var favoriteCount: Int = 0
) {
    fun isActive(): Boolean = status == McplAdvertisementStatus.ACTIVE
    fun isOwnedBy(userId: MkplUserId): Boolean = authorId == userId
    companion object {
        private val NONE = MkplAdvertisement()
    }
}