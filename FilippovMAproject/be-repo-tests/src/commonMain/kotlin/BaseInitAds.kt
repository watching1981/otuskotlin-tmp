package com.github.watching1981.backend.repo.tests

import com.github.watching1981.common.models.*
import kotlinx.datetime.Instant

abstract class BaseInitAds(private val op: String): IInitObjects<MkplAdvertisement> {
    private var idCounter = 1000L  // Счётчик для уникальных ID
    open val lockOld: MkplAdLock = MkplAdLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: MkplAdLock = MkplAdLock("20000000-0000-0000-0000-000000000009")
    val testInstant = Instant.fromEpochSeconds(1705248000) // 2024-01-15 00:00:00 UTC
    fun createInitTestModel(
        suf: String,
        authorId: MkplUserId = MkplUserId(123),
        fixedDate: Instant = testInstant,
        lock: MkplAdLock = lockOld,
    ) = MkplAdvertisement(

        id = MkplAdvertisementId(idCounter++),
        title = "$suf stub",
        description = "$suf stub description",
        location = "Санкт-Петербург",
        price = 2500000.0,
        authorId = authorId,
        status = McplAdvertisementStatus.DRAFT,
        lock = lock,
        createdAt = fixedDate,
        updatedAt = fixedDate
    )
    companion object {
        // В тестах использовать одинаковую дату
        val TEST_DATE = Instant.fromEpochSeconds(1705248000)
    }
}
