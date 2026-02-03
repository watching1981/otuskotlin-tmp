package com.github.watching1981.app.spring.repo

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import com.github.watching1981.app.spring.config.AdConfig
import com.github.watching1981.app.spring.controllers.AdControllerV1Fine
import com.github.watching1981.common.models.McplAdvertisementStatus
import com.github.watching1981.common.models.MkplAdvertisement
import com.github.watching1981.common.models.MkplAdvertisementId
import com.github.watching1981.common.models.MkplUserId
import com.github.watching1981.common.repo.*

import com.github.watching1981.repo.common.AdRepoInitialized
import com.github.watching1981.repo.inmemory.AdRepoInMemory
import com.github.watching1981.stubs.MkplAdStub
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.springframework.context.annotation.Import
import kotlin.test.Test
import kotlin.time.Duration.Companion.minutes

// Temporary simple test with stubs
@WebFluxTest(AdControllerV1Fine::class, AdConfig::class,
    properties = ["spring.main.allow-bean-definition-overriding=true"]
)
@Import(RepoInMemoryConfig::class)
internal class AdRepoInMemoryV1Test : AdRepoBaseV1Test() {
    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testTestRepo: IRepoAd

    @BeforeEach
    fun tearUp() {
        // Создаем слоты для захвата аргументов
        val slotAd = slot<DbAdRequest>()
        val slotId = slot<DbAdIdRequest>()
        val slotFl = slot<DbAdFilterRequest>()
       //инициализируем репозиторий AdRepoInMemory, создаем initObjects для добавления в кэш репозитория объявлений-заглушек
        //для дальнейшей работы с ними (получение, удаление, обновление, поиск) перед каждым тестом
        val repo = AdRepoInitialized(
            repo = AdRepoInMemory(randomUuid = { testAdId.toLong() }),
            initObjects = MkplAdStub.prepareSearchList("xx", McplAdvertisementStatus.DRAFT) + MkplAdStub.take()
        )
        //функции для мокирования действий с объявлением в репозитории AdRepoInMemory
        //capture(slotId) - захват аргумента в слот slotId
        //coAnswers { repo.readAd(slotId.captured) } - ответ мока при передаче захваченного аргумента реальному репозиторию

        //при вызове createAd - делегируем реальному репозиторию
        coEvery { testTestRepo.createAd(capture(slotAd)) } coAnswers { repo.createAd(slotAd.captured) }
        //при вызове readAd - делегируем реальному репозиторию
        coEvery { testTestRepo.readAd(capture(slotId)) } coAnswers { repo.readAd(slotId.captured) }
        coEvery { testTestRepo.updateAd(capture(slotAd)) } coAnswers { repo.updateAd(slotAd.captured) }
        coEvery { testTestRepo.deleteAd(capture(slotId)) } coAnswers { repo.deleteAd(slotId.captured) }
        coEvery { testTestRepo.searchAd(capture(slotFl)) } coAnswers { repo.searchAd(slotFl.captured) }
    }

    @Test
    override fun createAd() = super.createAd()

    @Test
    override fun readAd() = super.readAd()

    @Test
    override fun updateAd() = super.updateAd()

    @Test
    override fun deleteAd() = super.deleteAd()

    @Test
    override fun searchAd() = super.searchAd()


}
