package com.github.watching1981.backend.repo.tests

import com.github.watching1981.common.models.MkplAdvertisement
import com.github.watching1981.common.models.MkplAdvertisementId
import com.github.watching1981.common.repo.DbAdIdRequest
import com.github.watching1981.common.repo.DbAdResponseErr
import com.github.watching1981.common.repo.DbAdResponseOk
import com.github.watching1981.common.repo.IRepoAd
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoAdReadTest {
    abstract val repo: IRepoAd
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readAd(DbAdIdRequest(readSucc.id))

        assertIs<DbAdResponseOk>(result)
        assertEquals(readSucc.title, result.data.title)
        assertEquals(readSucc.id, result.data.id)
        assertEquals(readSucc.description, result.data.description)
        assertEquals(readSucc.car.brand, result.data.car.brand)
        assertEquals(readSucc.car.engine.type, result.data.car.engine.type)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readAd(DbAdIdRequest(notFoundId))

        assertIs<DbAdResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitAds("read") {
        override val initObjects: List<MkplAdvertisement> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = MkplAdvertisementId(0)

    }
}
