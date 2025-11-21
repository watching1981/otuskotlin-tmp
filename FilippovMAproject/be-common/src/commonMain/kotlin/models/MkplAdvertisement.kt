package com.github.watching1981.common.models
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant



data class MkplAdvertisement(
    val id: McplAdvertisementId =McplAdvertisementId.NONE,
    val title: String = "",
    val description: String ="",
    val price: Double = 0.0,
    val car: MkplCar = MkplCar.NONE,
//    val car: MkplCar = MkplCar (
//        brand = "",
//        model = "",
//        year = 0,
//        mileage = 0,
//        color =  "",
//        engine = MkplEngine(
//            type = MkplEngineType.GASOLINE,
//            volume = 0.0,
//            horsePower = 0
//        ),
//        transmission = MkplTransmission.MANUAL
//
//    ),
    val status: McplAdvertisementStatus=McplAdvertisementStatus.DRAFT,
    val location: String ="",
    val contactPhone: String="",
    val authorId: MkplUserId=MkplUserId.NONE,
    var lock: MkplAdLock = MkplAdLock.NONE,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
    val viewsCount: Int = 0,
    val favoriteCount: Int = 0
) {
    fun isActive(): Boolean = status == McplAdvertisementStatus.ACTIVE
    fun isOwnedBy(userId: MkplUserId): Boolean = authorId == userId
    companion object {
        private val NONE = MkplAdvertisement()
    }
}