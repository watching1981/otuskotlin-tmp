package com.github.watching1981.api.v2.mappers

import com.github.watching1981.api.v2.models.*
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import com.github.watching1981.common.stubs.MkplStubs
import com.github.watching1981.stubs.MkplAdStub
import kotlin.test.assertEquals
import kotlin.test.Test

class MapperUpdateTest {
    @Test
    fun fromTransport() {
        val req = AdUpdateRequest(
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS,
            ),
            ad = MkplAdStub.get().toTransportUpdateAd(),
        )
        val expected = MkplAdStub.prepareResult {
            ownerId = MkplUserId.NONE
            permissionsClient.clear()
        }

        val context = MkplContext()
        context.fromTransport(req)

        assertEquals(MkplStubs.SUCCESS, context.stubCase)
        assertEquals(MkplWorkMode.STUB, context.workMode)
        assertEquals(expected, context.adRequest)
    }

    @Test
    fun toTransport() {
        val context = MkplContext(
            requestId = MkplRequestId("1234"),
            command = MkplCommand.UPDATE,
            adResponse = MkplAdStub.get(),
            errors = mutableListOf(
                MkplError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MkplState.RUNNING,
        )

        val req = context.toTransportAd() as AdUpdateResponse

        assertEquals(MkplAdStub.get().toTransportAd(), req.ad)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
