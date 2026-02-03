package com.github.watching1981.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import com.github.watching1981.backend.repo.tests.*
import com.github.watching1981.common.models.MkplAdvertisement
import com.github.watching1981.repo.common.AdRepoInitialized
import com.github.watching1981.repo.common.IRepoAdInitializable
import java.io.File
import java.time.Duration
import kotlin.test.AfterTest
import kotlin.test.Ignore


private fun IRepoAdInitializable.clear() {
    val pgRepo = (this as AdRepoInitialized).repo as RepoAdSql  //pgRepo - sql-репозиторий на postgres
    pgRepo.clear()
}

@RunWith(Enclosed::class)
class RepoAdSQLTest {
//RepoAdSQLCreateTest наследуется от RepoAdCreateTest из модуля be-repo-tests
// Внутри RepoAdCreateTest будет запущен метод createAd свой для каждого репозитория
    class RepoAdSQLCreateTest : RepoAdCreateTest() {
        override val repo = repoUnderTestContainer( //инициализируем sql репозиторий RepoAdSql
            initObjects,
            randomUuid = { uuidNew.asLong().toString() },
        )

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoAdSQLReadTest : RepoAdReadTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoAdSQLUpdateTest : RepoAdUpdateTest() {
        override val repo = repoUnderTestContainer(
            initObjects,
            randomUuid = { lockNew.asString() },
        )

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoAdSQLDeleteTest : RepoAdDeleteTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    class RepoAdSQLSearchTest : RepoAdSearchTest() {
        override val repo = repoUnderTestContainer(initObjects)

        @AfterTest
        fun tearDown() = repo.clear()
    }

    @Ignore
    companion object {
        private const val PG_SERVICE = "psql"
        private const val MG_SERVICE = "liquibase"
        private val container: ComposeContainer by lazy {
            val res = this::class.java.classLoader.getResource("docker-compose-pg.yml")
                ?: throw Exception("No resource found")
            val file = File(res.toURI())
            ComposeContainer(
                file,
            )
                .withExposedService(PG_SERVICE, 5432)
                .withStartupTimeout(Duration.ofSeconds(300))
                .waitingFor(
                    MG_SERVICE,
                    Wait.forLogMessage(".*Liquibase command 'update' was executed successfully.*", 1)
                )
        }

        private const val HOST = "localhost"
        private const val USER = "postgres"
        private const val PASS = "car-pass"
        private val PORT by lazy {
            container.getServicePort(PG_SERVICE, 5432) ?: 5432
        }

        fun repoUnderTestContainer(
            initObjects: Collection<MkplAdvertisement> = emptyList(),
            randomUuid: () -> String = { uuid4().toString() },
        ): IRepoAdInitializable = AdRepoInitialized(
            repo = RepoAdSql(   //задаем репозиторий SQL
                SqlProperties(
                    host = HOST,
                    user = USER,
                    password = PASS,
                    port = PORT,
                ),
                randomLockUuid = randomUuid
            ),
            initObjects = initObjects,
        )

        @JvmStatic
        @BeforeClass
        fun start() {
            container.start()
        }

        @JvmStatic
        @AfterClass
        fun finish() {
            container.stop()
        }
    }
}

