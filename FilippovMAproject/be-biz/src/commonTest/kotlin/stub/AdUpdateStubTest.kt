package com.github.watching1981.biz.stub

import kotlinx.coroutines.test.runTest
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import com.github.watching1981.common.stubs.MkplStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class AdUpdateStubTest {

    private val processor = MkplAdProcessor()
    val id:McplAdvertisementId = McplAdvertisementId(1)
    val title = "title 666"
    val description = "desc 666"


    @Test
    fun create() = runTest {

        val ctx = MkplContext(
            command = MkplCommand.UPDATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.SUCCESS,
            adRequest = MkplAdvertisement(
                id = id,
                title = title,
                description = description,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.adResponse.id)
        assertEquals(title, ctx.adResponse.title)
        assertEquals(description, ctx.adResponse.description)
    }

    @Test
    fun badId() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.UPDATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_TITLE,
            adRequest = MkplAdvertisement(
                id = id,
                title = "",
                description = description,
            ),
        )
        processor.exec(ctx)
        assertEquals(McplAdvertisementId.NONE, ctx.adResponse.id)
        assertEquals("", ctx.adResponse.title)
        assertEquals("", ctx.adResponse.description)
        assertEquals(0.0, ctx.adResponse.price)
        assertEquals(MkplCar.NONE, ctx.adResponse.car)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.UPDATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_DESCRIPTION,
            adRequest = MkplAdvertisement(
                id = id,
                title = title,
                description = "",
            ),
        )
        processor.exec(ctx)
        assertEquals(McplAdvertisementId.NONE, ctx.adResponse.id)
        assertEquals("", ctx.adResponse.title)
        assertEquals("", ctx.adResponse.description)
        assertEquals(0.0, ctx.adResponse.price)
        assertEquals(MkplCar.NONE, ctx.adResponse.car)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.UPDATE,
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
            command = MkplCommand.UPDATE,
            state = MkplState.NONE,
            workMode = MkplWorkMode.STUB,
            stubCase = MkplStubs.BAD_SEARCH_STRING,
            adRequest = MkplAdvertisement(
                id = id,
                title = title,
                description = description,
            ),
        )
        processor.exec(ctx)
        assertEquals(McplAdvertisementId.NONE, ctx.adResponse.id)
        assertEquals("", ctx.adResponse.title)
        assertEquals("", ctx.adResponse.description)
        assertEquals(0.0, ctx.adResponse.price)
        assertEquals(MkplCar.NONE, ctx.adResponse.car)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
