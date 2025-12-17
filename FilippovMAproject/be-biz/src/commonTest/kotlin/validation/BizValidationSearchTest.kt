package com.github.watching1981.biz.validation

import kotlinx.coroutines.test.runTest
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = MkplCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adFilterRequest = MkplAdvertisementSearch(MkplAdvertisementFilters(brand = "BYD"),MkplPagination(),MkplSortOptions()),
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(MkplState.FAILING, ctx.state)
    }
    @Test fun correctPagination() = validationYearRangeCorrect(command, processor)
    @Test fun  badPagination() = validationYearBadRange(command, processor)
}
