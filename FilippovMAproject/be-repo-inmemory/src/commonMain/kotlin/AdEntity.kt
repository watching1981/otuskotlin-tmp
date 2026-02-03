package com.github.watching1981.repo.inmemory

import com.github.watching1981.common.models.*
import kotlinx.datetime.Instant

data class AdEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val authorId: String? = null,
    val price: String? = null,
    val location: String? = null,
    val status: String? = null,
    val lock: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {
    constructor(model: MkplAdvertisement): this(
        id = model.id.takeIf { it != MkplAdvertisementId.NONE }?.asLong()?.toString(),
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        authorId = model.authorId.takeIf { it != MkplUserId.NONE }?.value?.toString(),
        price = model.price?.toString(),
        location = model.location.takeIf { it.isNotBlank() },
        status = model.status?.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() },
        createdAt = model.createdAt?.toString(),
        updatedAt = model.updatedAt?.toString()


    )

fun toInternal()= MkplAdvertisement(
        id = id?.toLongOrNull()?.let { MkplAdvertisementId(it) } ?: MkplAdvertisementId.NONE,
        title = title ?: "",
        description = description ?: "",
        authorId = authorId?.toLongOrNull()?.let { MkplUserId(it) } ?: MkplUserId.NONE,
        lock = lock?.let { MkplAdLock(it) } ?: MkplAdLock.NONE,
        price = price!!.toDouble(),
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
