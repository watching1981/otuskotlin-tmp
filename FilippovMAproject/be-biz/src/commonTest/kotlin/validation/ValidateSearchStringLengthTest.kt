package com.github.watching1981.biz.validation

import com.github.watching1981.biz.validation.ValidateTitleHasContentTest.Companion.chain
import kotlinx.coroutines.test.runTest
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
//import com.github.watching1981.common.models.MkplAdFilter
import com.github.watching1981.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adFilterValidating = MkplAdvertisementSearch(
            MkplAdvertisementFilters(brand = ""),
            MkplPagination(),
            MkplSortOptions()
        )
        )

        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adFilterValidating = MkplAdvertisementSearch(MkplAdvertisementFilters(brand = " "),MkplPagination(),MkplSortOptions()))
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adFilterValidating = MkplAdvertisementSearch(MkplAdvertisementFilters(brand = "12"),MkplPagination(),MkplSortOptions()))
        chain.exec(ctx)
        assertEquals(MkplState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-brand-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adFilterValidating = MkplAdvertisementSearch(MkplAdvertisementFilters(brand = "Toyota"),MkplPagination(),MkplSortOptions()))
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adFilterValidating = MkplAdvertisementSearch(MkplAdvertisementFilters(brand = "Toyota".repeat(31)),MkplPagination(),MkplSortOptions()))
        chain.exec(ctx)
        assertEquals(MkplState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-brand-too-long", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateBrandLength("")
        }.build()
    }
}
