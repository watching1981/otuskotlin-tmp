import org.junit.Test
import com.github.watching1981.api.v1.models.AdCreateRequest
import com.github.watching1981.api.v1.models.AdCreateResponse
import com.github.watching1981.api.v1.models.AdDebug
import com.github.watching1981.api.v1.models.AdRequestDebugMode
import com.github.watching1981.api.v1.models.AdRequestDebugStubs
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.MkplAdId
import com.github.watching1981.common.models.MkplAdLock
import com.github.watching1981.common.models.MkplCommand
import com.github.watching1981.common.models.MkplError
import com.github.watching1981.common.models.MkplRequestId
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.common.models.MkplUserId
import com.github.watching1981.common.models.MkplWorkMode
import com.github.watching1981.common.stubs.MkplStubs
import com.github.watching1981.mappers.v1.fromTransport
import com.github.watching1981.mappers.v1.toTransportAd
import com.github.watching1981.mappers.v1.toTransportCreateAd
import com.github.watching1981.stubs.MkplAdStub
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = AdCreateRequest(
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS,
            ),
            ad = MkplAdStub.get().toTransportCreateAd()
        )
        val expected = MkplAdStub.prepareResult {
            id = MkplAdId.NONE
            ownerId = MkplUserId.NONE
            lock = MkplAdLock.NONE
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
            command = MkplCommand.CREATE,
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

        val req = context.toTransportAd() as AdCreateResponse

        assertEquals(req.ad, MkplAdStub.get().toTransportAd())
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
