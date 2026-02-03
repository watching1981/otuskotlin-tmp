package com.github.watching1981.backend.repo.postgresql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import com.github.watching1981.common.models.*
import jdk.jfr.internal.event.EventConfiguration.timestamp
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.sql.Timestamp

class AdTable(tableName: String) : Table(tableName) {
    val id = long(SqlFields.ID)
    val title = text(SqlFields.TITLE).nullable()
    val description = text(SqlFields.DESCRIPTION).nullable()
    val author_id = long(SqlFields.AUTHOR_ID)
    val lock = text(SqlFields.LOCK)
    val brand = text(SqlFields.BRAND)
    var model= text(SqlFields.MODEL)
    var status= text(SqlFields.STATUS)
    var year= integer(SqlFields.YEAR)
    var mileage= integer(SqlFields.MILEAGE)
    var location= text(SqlFields.LOCATION)
    var price= double(SqlFields.PRICE)
    var created_at= text(SqlFields.CREATED_AT)

    var updated_at= text(SqlFields.UPDATED_AT)
    val engine_type = engineTypeEnumeration(SqlFields.ENGINE_TYPE)
    val transmission = transmissionEnumeration(SqlFields.TRANSMISSION)




    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = MkplAdvertisement(
        id = MkplAdvertisementId(res[id]),
        title = res[title] ?: "",
        description = res[description] ?: "",
        price = res[price].toDouble(),
        car = MkplCar(brand = res[brand], model = res[model], year =res[year].toInt(), mileage = res[mileage].toInt(), engine = MkplEngine(type = res[engine_type],volume = 2.0, horsePower =100 ),transmission = res[transmission] ),
        status = McplAdvertisementStatus.valueOf(res[status]),
        authorId = MkplUserId(res[author_id]),
        lock = MkplAdLock(res[lock]),
        location  = res[location] ?: "",
        createdAt = res[created_at]?.let { Instant.parse(it) },
        updatedAt = res[updated_at]?.let { Instant.parse(it) },
    )


fun UpdateBuilder<*>.to(ad: MkplAdvertisement, randomLockUuid: () -> String, randomUuid: () -> Long) {
    this[id] = ad.id.takeIf { it != MkplAdvertisementId.NONE }?.asLong() ?: randomUuid()
    this[title] = ad.title
    this[description] = ad.description
    this[author_id] = ad.authorId.value
    this[lock] = ad.lock.takeIf { it != MkplAdLock.NONE }?.asString() ?: randomLockUuid()
    this[brand] = ad.car.brand
    this[model] = ad.car.model
    this[status] = ad.status.name
    this[engine_type] = ad.car.engine.type
    this[transmission] = ad.car.transmission
    this[year] = ad.car.year
    this[mileage] = ad.car.mileage
    this[location] = ad.location
    this[price] = ad.price
    this[created_at] = ad.createdAt.toString()
    this[updated_at] = ad.updatedAt.toString()

}

}

