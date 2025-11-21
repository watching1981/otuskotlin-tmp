//package com.github.watching1981.stubs
//
//import com.github.watching1981.common.models.MkplAd
//import com.github.watching1981.common.models.McplAdvertisementId
//import com.github.watching1981.common.models.MkplEngine
//import com.github.watching1981.stubs.MkplAdStubCars.AD_DEMAND_CAR1
//import com.github.watching1981.stubs.MkplAdStubCars.AD_SUPPLY_CAR1
//
//object MkplAdStub {
//    fun get(): MkplAd = AD_DEMAND_CAR1.copy()
//
//    fun prepareResult(block: MkplAd.() -> Unit): MkplAd = get().apply(block)
//
//
//
//    fun prepareOffersList(filter: String, type: MkplEngine) = listOf(
//        mkplAdSupply("18345", filter, type),
//        mkplAdSupply("18346", filter, type),
//        mkplAdSupply("18347", filter, type),
//        mkplAdSupply("18348", filter, type),
//        mkplAdSupply("18349", filter, type),
//        mkplAdSupply("18350", filter, type),
//    )
//
//    private fun mkplAdDemand(id: String, filter: String, type: MkplEngine) =
//        mkplAd(AD_DEMAND_CAR1, id = id, filter = filter, type = type)
//
//    private fun mkplAdSupply(id: String, filter: String, type: MkplEngine) =
//        mkplAd(AD_SUPPLY_CAR1, id = id, filter = filter, type = type)
//
//    private fun mkplAd(base: MkplAd, id: String, filter: String, type: MkplEngine) = base.copy(
//        id = McplAdvertisementId(id),
//        title = "$filter $id",
//        description = "desc $filter $id",
//        adType = type,
//    )
//
//}
