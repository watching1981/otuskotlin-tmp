package com.github.watching1981.biz.stub

import kotlinx.coroutines.test.runTest
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import com.github.watching1981.common.stubs.MkplStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class AdSearchStubTest {
    val filter = MkplAdvertisementFilters(brand = "Toyota")
    private val processor = MkplAdProcessor()
    @Test
    fun read() = runTest {

        val ctx = MkplContext(
            command = MkplCommand.SEARCH,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.SUCCESS,
            adFilterRequest = MkplAdvertisementSearch(
                filters = filter,
                pagination = MkplPagination(),
                sort = MkplSortOptions()
            ),
        )
        processor.exec(ctx)

        assertEquals(MkplState.FINISHING, ctx.state)
        assertTrue(ctx.adsResponse.isNotEmpty())
        // Проверяем, что все результаты соответствуют фильтру
        ctx.adsResponse.forEach { ad ->
            assertEquals("Toyota", ad.car.brand)
        }
        // Проверяем первый результат
        val firstAd = ctx.adsResponse.first()
        assertEquals("Toyota", firstAd.car.brand)
    }

    @Test
    fun badId() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.SEARCH,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_ID,
            adFilterRequest = MkplAdvertisementSearch(
                filters = filter,
                pagination = MkplPagination(),
                sort = MkplSortOptions()
            ),
        )
        processor.exec(ctx)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)

        assertEquals(McplAdvertisementId.NONE, ctx.adResponse.id)
        assertEquals("", ctx.adResponse.title)
        assertEquals("", ctx.adResponse.description)
        assertEquals(0.0, ctx.adResponse.price)
        assertEquals(MkplCar.NONE, ctx.adResponse.car)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.SEARCH,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.DB_ERROR,
            adFilterRequest = MkplAdvertisementSearch(
                filters = filter,
                pagination = MkplPagination(),
                sort = MkplSortOptions()
            ),
        )
        processor.exec(ctx)
        assertEquals(McplAdvertisementId.NONE, ctx.adResponse.id)
        assertEquals("", ctx.adResponse.title)
        assertEquals("", ctx.adResponse.description)
        assertEquals(0.0, ctx.adResponse.price)
        assertEquals(MkplCar.NONE, ctx.adResponse.car)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.SEARCH,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_TITLE,
            adFilterRequest = MkplAdvertisementSearch(
                filters = filter,
                pagination = MkplPagination(),
                sort = MkplSortOptions()
            ),
        )
        processor.exec(ctx)
        assertEquals(McplAdvertisementId.NONE, ctx.adResponse.id)
        assertEquals("", ctx.adResponse.title)
        assertEquals("", ctx.adResponse.description)
        assertEquals(0.0, ctx.adResponse.price)
        assertEquals(MkplCar.NONE, ctx.adResponse.car)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
