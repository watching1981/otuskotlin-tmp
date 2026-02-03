package com.github.watching1981.biz.stub

import kotlinx.coroutines.test.runTest
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import com.github.watching1981.common.stubs.MkplStubs
import com.github.watching1981.stubs.MkplAdStub
import kotlin.test.Test
import kotlin.test.assertEquals

class AdGetStubTest {

    private val processor = MkplAdProcessor()
    val id = MkplAdvertisementId.NONE

    @Test
    fun read() = runTest {

        val ctx = MkplContext(
            command = MkplCommand.GET,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.SUCCESS,
            adRequest = MkplAdvertisement(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (MkplAdStub.get()) {
            assertEquals(id, ctx.adResponse.id)
            assertEquals(title, ctx.adResponse.title)
            assertEquals(description, ctx.adResponse.description)
            assertEquals(price, ctx.adResponse.price)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.GET,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_ID,
            adRequest = MkplAdvertisement(),
        )
        processor.exec(ctx)
        assertEquals(MkplAdvertisementId.NONE, ctx.adResponse.id)
        assertEquals("", ctx.adResponse.title)
        assertEquals("", ctx.adResponse.description)
        assertEquals(0.0, ctx.adResponse.price)
        assertEquals(MkplCar.NONE, ctx.adResponse.car)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.GET,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.DB_ERROR,
            adRequest = MkplAdvertisement(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplAdvertisementId.NONE, ctx.adResponse.id)
        assertEquals("", ctx.adResponse.title)
        assertEquals("", ctx.adResponse.description)
        assertEquals(0.0, ctx.adResponse.price)
        assertEquals(MkplCar.NONE, ctx.adResponse.car)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.GET,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_TITLE,
            adRequest = MkplAdvertisement(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplAdvertisementId.NONE, ctx.adResponse.id)
        assertEquals("", ctx.adResponse.title)
        assertEquals("", ctx.adResponse.description)
        assertEquals(0.0, ctx.adResponse.price)
        assertEquals(MkplCar.NONE, ctx.adResponse.car)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
