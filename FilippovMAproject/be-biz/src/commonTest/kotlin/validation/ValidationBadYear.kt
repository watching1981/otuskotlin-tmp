package com.github.watching1981.biz.validation

import kotlinx.coroutines.test.runTest
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import com.github.watching1981.stubs.MkplAdStub
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = MkplAdStub.get()

fun validationYearRangeCorrect(command: MkplCommand, processor: MkplAdProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adFilterRequest = MkplAdvertisementSearch(
            MkplAdvertisementFilters(brand = "BYD", minYear = 2020, maxYear = 2022),
            MkplPagination(page = 1, size = 20),
            MkplSortOptions()),
    )

    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkplState.FAILING, ctx.state)

}



fun validationYearBadRange(command: MkplCommand, processor: MkplAdProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adFilterRequest = MkplAdvertisementSearch(
            MkplAdvertisementFilters(brand = "BYD", minYear = 1980, maxYear = 2022),
            MkplPagination(page = 1, size = 20),
            MkplSortOptions()),
    )

    processor.exec(ctx)

    assertEquals(MkplState.FAILING, ctx.state)
    assertEquals(1, ctx.errors.size)
    assertEquals("validation-yearRange-invalid-range", ctx.errors.first().code)

}