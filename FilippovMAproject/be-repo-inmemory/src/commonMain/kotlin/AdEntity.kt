package com.github.watching1981.repo.inmemory

import com.github.watching1981.common.models.*
import kotlinx.datetime.Instant

data class AdEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val authorId: String? = null,
    val price: String? = null,
    val brand:String? = null,
    val model:String? = null,
    val year:String? = null,
    val mileage:String? = null,
    val color:String? = null,
    val engineType:String? = null,
    val engineVolume:String? = null,
    val horsePower:String? = null,
    val transmission:String? = null,
    val location: String? = null,
    val status: String? = null,
    val lock: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val contactPhone: String? = null
) {
    constructor(model: MkplAdvertisement): this(
        id = model.id.takeIf { it != MkplAdvertisementId.NONE }?.asLong()?.toString(),
        lock = model.lock.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        authorId = model.authorId.takeIf { it != MkplUserId.NONE }?.value?.toString(),
        price = model.price?.toString(),
        brand = model.car.brand,
        model = model.car.model,
        year = model.car.year?.toString(),
        mileage = model.car.mileage?.toString(),
        color = model.car.color,
        engineType = model.car.engine.type?.toString(),
        engineVolume = model.car.engine.volume?.toString(),
        horsePower = model.car.engine.horsePower?.toString(),
        transmission = model.car.transmission?.toString(),
        location = model.location.takeIf { it.isNotBlank() },
        status = model.status?.name,
        contactPhone = model.contactPhone,
        createdAt = model.createdAt?.toString(),
        updatedAt = model.updatedAt?.toString()


    )

fun toInternal()= MkplAdvertisement(
        id = id?.toLongOrNull()?.let { MkplAdvertisementId(it) } ?: MkplAdvertisementId.NONE,
        lock = lock?.let { MkplAdLock(it) } ?: MkplAdLock.NONE,
        title = title ?: "",
        description = description ?: "",
        authorId = authorId?.toLongOrNull()?.let { MkplUserId(it) } ?: MkplUserId.NONE,
        price = price!!.toDouble(),
        car = MkplCar(brand = brand.toString(),model = model.toString(), year = year?.toInt() ?: 0, mileage = mileage?.toInt() ?: 0, color = color.toString(), engine = MkplEngine(type = MkplEngineType.valueOf(engineType.toString())  , volume = engineVolume?.toDouble()
            ?: 0.0, horsePower = horsePower?.toInt()), transmission = MkplTransmission.valueOf(transmission.toString()) ) ,
        location = location ?: "",
        status = status?.let {
            McplAdvertisementStatus.values().find { st -> st.name == it }
        } ?: McplAdvertisementStatus.ACTIVE,
        createdAt = createdAt?.toInstantOrNull(),
        updatedAt = updatedAt?.toInstantOrNull()
    )
    private fun String.toInstantOrNull(): Instant? {
        return try {
            Instant.parse(this)
        } catch (e:Throwable) {
            null
        }
    }
}
