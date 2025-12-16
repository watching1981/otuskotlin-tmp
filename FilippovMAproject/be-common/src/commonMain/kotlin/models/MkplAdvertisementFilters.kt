package com.github.watching1981.common.models

data class MkplAdvertisementFilters(
    var brand: String? = null,
    var model: String? = null,
    val minYear: Int? = null,
    val maxYear: Int? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val engineTypes: Set<MkplEngineType> = emptySet(),
    val transmissions: Set<MkplTransmission> = emptySet(),
    val minMileage: Int? = null,
    val maxMileage: Int? = null,
    val location: String? = null,
    val status: McplAdvertisementStatus? = McplAdvertisementStatus.ACTIVE
){
    companion object{
        val NONE = MkplAdvertisementFilters()
    }
}
