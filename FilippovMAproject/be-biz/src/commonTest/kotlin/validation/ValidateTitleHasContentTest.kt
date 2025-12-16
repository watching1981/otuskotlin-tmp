package com.github.watching1981.biz.validation

import kotlinx.coroutines.test.runTest
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
//import com.github.watching1981.common.models.MkplAdFilter
import com.github.watching1981.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateTitleHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adValidating = MkplAdvertisement(title = ""))
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adValidating = MkplAdvertisement(title = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(MkplState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-title-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = MkplContext(state = MkplState.RUNNING, adFilterValidating = MkplAdvertisementSearch(
            MkplAdvertisementFilters(brand = "BYD"),
            MkplPagination(),
            MkplSortOptions()
        )
        )
        chain.exec(ctx)
        assertEquals(MkplState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateTitleHasContent("")
        }.build()
    }
}
