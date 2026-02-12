package com.github.watching1981.backend.repo.tests

import kotlinx.coroutines.test.runTest

import com.github.watching1981.common.models.MkplAdvertisement
import com.github.watching1981.common.repo.*
import com.github.watching1981.stubs.MkplAdStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AdRepositoryMockTest {
    private val repo = AdRepositoryMock(
        invokeCreateAd = { DbAdResponseOk(MkplAdStub.prepareResult { title = "create" }) },
        invokeReadAd = { DbAdResponseOk(MkplAdStub.prepareResult { title = "read" }) },
        invokeUpdateAd = { DbAdResponseOk(MkplAdStub.prepareResult { title = "update" }) },
        invokeDeleteAd = { DbAdResponseOk(MkplAdStub.prepareResult { title = "delete" }) },
        invokeSearchAd = { DbAdsResponseOk(listOf(MkplAdStub.prepareResult { title = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createAd(DbAdRequest(MkplAdvertisement()))
        assertIs<DbAdResponseOk>(result)
        assertEquals("create", result.data.title)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readAd(DbAdIdRequest(MkplAdvertisement()))
        assertIs<DbAdResponseOk>(result)
        assertEquals("read", result.data.title)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateAd(DbAdRequest(MkplAdvertisement()))
        assertIs<DbAdResponseOk>(result)
        assertEquals("update", result.data.title)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteAd(DbAdIdRequest(MkplAdvertisement()))
        assertIs<DbAdResponseOk>(result)
        assertEquals("delete", result.data.title)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchAd(DbAdFilterRequest())
        assertIs<DbAdsResponseOk>(result)
        assertEquals("search", result.data.first().title)
    }

}
