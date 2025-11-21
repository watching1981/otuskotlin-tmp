package com.github.watching1981.common.models

data class MkplCar (
    val brand: String="",
    val model: String="",
    val year: Int=0,
    val mileage: Int=0,
    val color: String="",
    val engine: MkplEngine=MkplEngine(
        type = MkplEngineType.GASOLINE,
        volume = 0.0,
        horsePower = 0
    ),
    val transmission: MkplTransmission=MkplTransmission.MANUAL
)
{
    companion object {
        val NONE = MkplCar()
    }
}