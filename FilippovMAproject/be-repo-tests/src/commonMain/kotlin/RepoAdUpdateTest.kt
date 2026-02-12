package com.github.watching1981.backend.repo.tests

import com.github.watching1981.common.models.*
import com.github.watching1981.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull


abstract class RepoAdUpdateTest {
    abstract val repo: IRepoAd
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = MkplAdvertisementId(-100)
    protected val lockBad = MkplAdLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = MkplAdLock("20000000-0000-0000-0000-000000000001")
    protected val updateEmptyId = MkplAdvertisementId.NONE

    private val reqUpdateSucc by lazy {
        MkplAdvertisement(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            authorId = MkplUserId(123),
            lock = initObjects.first().lock,

        )
    }
    private val reqUpdateNotFound = MkplAdvertisement(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        authorId = MkplUserId(123),
        lock = initObjects.first().lock,

    )
    private val reqUpdateConc by lazy {
        MkplAdvertisement(
            id = updateConc.id,
            title = "update object not found",
            description = "update object not found description",
            authorId = MkplUserId(123),
            lock = lockBad,
        )
    }

    private val reqUpdateEmptyId = MkplAdvertisement(
        id = updateEmptyId,
        title = "update empty Id",
        description = "update object empty Id description",
        authorId = MkplUserId(123),
        lock = initObjects.first().lock,

        )


    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateAd(DbAdRequest(reqUpdateSucc))
        assertIs<DbAdResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.title, result.data.title)
        assertEquals(reqUpdateSucc.description, result.data.description)
        assertEquals(reqUpdateSucc.car, result.data.car)
    }
//
    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateAd(DbAdRequest(reqUpdateNotFound))
        assertIs<DbAdResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)

    }
    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateAd(DbAdRequest(reqUpdateConc))
        assertIs<DbAdResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }
    @Test
    fun updateEmptyId() = runRepoTest {
        val result = repo.updateAd(DbAdRequest(reqUpdateEmptyId))
        assertIs<DbAdResponseErr>(result)
        val error = result.errors.find { it.code == "repo-empty-id" }
        assertEquals("id", error?.field)

    }

    companion object : BaseInitAds("update") {
        override val initObjects: List<MkplAdvertisement> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }

}
