package com.github.watching1981.backend.repo.tests

import com.github.watching1981.common.models.*
import com.github.watching1981.common.repo.DbAdRequest
import com.github.watching1981.common.repo.DbAdResponseOk
import com.github.watching1981.repo.common.IRepoAdInitializable
import kotlin.test.*


abstract class RepoAdCreateTest {
    abstract val repo: IRepoAdInitializable
    protected open val uuidNew = MkplAdvertisementId(100000001)

    private val createObj = MkplAdvertisement(
        title = "create object",
        id = uuidNew,
        description = "create object description",
        authorId = MkplUserId(123),

    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createAd(DbAdRequest(createObj))
        val expected = createObj
        assertIs<DbAdResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.title, result.data.title)
        assertEquals(expected.description, result.data.description)
        assertNotEquals(MkplAdvertisementId.NONE, result.data.id)
    }

    companion object : BaseInitAds("create") {
        override val initObjects: List<MkplAdvertisement> = emptyList()
    }
}
