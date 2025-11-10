package com.github.watching1981.api.v2
import com.github.watching1981.api.v2.models.AdCreateObject
import com.github.watching1981.api.v2.models.AdCreateRequest
import com.github.watching1981.api.v2.models.AdDebug
import com.github.watching1981.api.v2.models.AdRequestDebugMode
import com.github.watching1981.api.v2.models.AdRequestDebugStubs
import com.github.watching1981.api.v2.models.AdStatus
import com.github.watching1981.api.v2.models.DealSide
import com.github.watching1981.api.v2.models.IRequest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV2SerializationTest {
    private val request: IRequest = AdCreateRequest(
        debug = AdDebug(
            mode = AdRequestDebugMode.STUB,
            stub = AdRequestDebugStubs.BAD_TITLE
        ),
        ad = AdCreateObject(
            title = "ad title",
            description = "ad description",
            adType = DealSide.DEMAND,
            status = AdStatus.ACTIVE,
        )
    )

    @Test
    fun serialize() {
        val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)

        println(json)

        assertContains(json, Regex("\"title\":\\s*\"ad title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(request)
        val obj = apiV2Mapper.decodeFromString<IRequest>(json) as AdCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"ad": null}
        """.trimIndent()
        val obj = apiV2Mapper.decodeFromString<AdCreateRequest>(jsonString)

        assertEquals(null, obj.ad)
    }
}
