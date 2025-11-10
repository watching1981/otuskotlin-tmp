package com.github.watching1981.stubs

import com.github.watching1981.common.models.*

object MkplAdStubCars {
    val AD_DEMAND_CAR1: MkplAd
        get() = MkplAd(
            id = MkplAdId("18345"),
            title = "Куплю автомобиль",
            description = "Toyota Camry не старше 2006 г",
            ownerId = MkplUserId("user-1"),
            adType = MkplDealSide.DEMAND,
            location = "",
            status = MkplStatus.ACTIVE,
            lock = MkplAdLock("123"),
            permissionsClient = mutableSetOf(
                MkplAdPermissionClient.READ,
                MkplAdPermissionClient.UPDATE,
                MkplAdPermissionClient.DELETE,
                MkplAdPermissionClient.MAKE_VISIBLE_PUBLIC,
                MkplAdPermissionClient.MAKE_VISIBLE_OWNER,
            )
        )
    val AD_SUPPLY_CAR1 = AD_DEMAND_CAR1.copy(adType = MkplDealSide.SUPPLY)
}
