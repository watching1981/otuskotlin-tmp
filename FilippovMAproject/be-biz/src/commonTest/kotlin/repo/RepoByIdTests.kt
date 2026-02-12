package repo

import kotlinx.coroutines.test.runTest
import com.github.watching1981.backend.repo.tests.AdRepositoryMock
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.models.*
import com.github.watching1981.common.repo.DbAdResponseOk
import com.github.watching1981.common.repo.errorNotFound
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initAd = MkplAdvertisement(
    id = MkplAdvertisementId(123),
    title = "abc",
    description = "abc",
)
private val repo = AdRepositoryMock(
        invokeReadAd = {
            if (it.id == initAd.id) {
                DbAdResponseOk(
                    data = initAd,
                )
            } else errorNotFound(it.id)
        }
    )
private val settings = MkplCorSettings(repoTest = repo)
private val processor = MkplAdProcessor(settings)

fun repoNotFoundTest(command: MkplCommand) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adRequest = MkplAdvertisement(
            id = MkplAdvertisementId(12345),
            title = "xyz",
            price = 1000000.0,
            description = "xyz",
            lock = MkplAdLock("123"),
        ),
    )
    processor.exec(ctx)
    assertEquals(MkplState.FAILING, ctx.state)
    assertEquals(MkplAdvertisement().title, ctx.adResponse.title)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
