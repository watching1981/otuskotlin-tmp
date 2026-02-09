package com.github.watching1981.stub

import kotlinx.coroutines.test.runTest
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import com.github.watching1981.common.stubs.MkplStubs
import com.github.watching1981.stubs.MkplAdStub
import kotlin.test.Test
import kotlin.test.assertEquals

class AdDeleteStubTest {

    private val processor = MkplAdProcessor()
    val id = McplAdvertisementId.NONE

    @Test
    fun delete() = runTest {

        val ctx = MkplContext(
            command = MkplCommand.DELETE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.SUCCESS,
            adRequest = MkplAdvertisement(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = MkplAdStub.get()
        assertEquals(stub.id, ctx.adResponse.id)
        assertEquals(stub.title, ctx.adResponse.title)
        assertEquals(stub.description, ctx.adResponse.description)
        assertEquals(stub.price, ctx.adResponse.price)
    }

    @Test
    fun badId() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.DELETE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_ID,
            adRequest = MkplAdvertisement(),
        )
        processor.exec(ctx)
        assertEquals(McplAdvertisementId.NONE, ctx.adResponse.id)
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
            command = MkplCommand.DELETE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.DB_ERROR,
            adRequest = MkplAdvertisement(
                id = id,
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
            command = MkplCommand.DELETE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_TITLE,
            adRequest = MkplAdvertisement(
                id = id,
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
