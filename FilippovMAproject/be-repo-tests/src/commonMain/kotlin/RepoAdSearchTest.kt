package com.github.watching1981.backend.repo.tests

import com.github.watching1981.common.models.MkplAdvertisement
import com.github.watching1981.common.models.MkplUserId
import com.github.watching1981.common.repo.DbAdFilterRequest
import com.github.watching1981.common.repo.DbAdsResponseOk
import com.github.watching1981.common.repo.IRepoAd
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoAdSearchTest {
    abstract val repo: IRepoAd

    protected open val initializedObjects: List<MkplAdvertisement> = initObjects


    @Test
    fun searchAuthor() = runRepoTest {
        val result = repo.searchAd(DbAdFilterRequest(authorId = searchAuthorId))
        assertIs<DbAdsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[2]).sortedBy { it.id.asLong() }
        assertEquals(expected, result.data.sortedBy { it.id.asLong() })
    }
    @Test
    fun searchTitle() = runRepoTest {
        val result = repo.searchAd(DbAdFilterRequest(titleFilter = "ad2"))
        assertIs<DbAdsResponseOk>(result)
        assertEquals(1, result.data.size)
        assertEquals("ad2 stub", result.data.first().title)
    }
    @Test
    fun searchDescription() = runRepoTest {
        val result = repo.searchAd(DbAdFilterRequest(descriptionFilter = "description"))
        assertIs<DbAdsResponseOk>(result)
        assertEquals(3, result.data.size) // Все 3 объявления содержат "description"
    }

    companion object: BaseInitAds("search") {

        val searchAuthorId = MkplUserId(124)
        override val initObjects: List<MkplAdvertisement> = listOf(
            createInitTestModel("ad1",authorId = MkplUserId(123),TEST_DATE),
            createInitTestModel("ad2", authorId = searchAuthorId,TEST_DATE),
            createInitTestModel("ad3", authorId = searchAuthorId,TEST_DATE),

        )
    }
}
