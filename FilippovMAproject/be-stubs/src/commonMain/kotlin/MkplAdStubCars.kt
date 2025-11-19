//package com.github.watching1981.stubs
//
//import com.github.watching1981.common.models.*
//
//object MkplAdStubCars {
//    val AD_DEMAND_CAR1: MkplAd
//        get() = MkplAd(
//            id = McplAdvertisementId("18345"),
//            title = "Куплю автомобиль",
//            description = "Toyota Camry не старше 2006 г",
//            ownerId = MkplUserId("user-1"),
//            adType = MkplEngine.DEMAND,
//            location = "",
//            status = McplAdvertisementStatus.ACTIVE,
//            lock = MkplAdLock("123"),
//            permissionsClient = mutableSetOf(
//                MkplAdvertisementSearch.READ,
//                MkplAdvertisementSearch.UPDATE,
//                MkplAdvertisementSearch.DELETE,
//                MkplAdvertisementSearch.MAKE_VISIBLE_PUBLIC,
//                MkplAdvertisementSearch.MAKE_VISIBLE_OWNER,
//            )
//        )
//    val AD_SUPPLY_CAR1 = AD_DEMAND_CAR1.copy(adType = MkplEngine.SUPPLY)
//}
