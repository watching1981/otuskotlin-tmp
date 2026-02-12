package com.github.watching1981.app.spring.config

import com.github.watching1981.backend.repo.postgresql.RepoAdSql
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.car.logging.common.MpLoggerProvider
import com.github.watching1981.logging.jvm.mpLoggerLogback
import com.github.watching1981.backend.repository.inmemory.AdRepoStub
import com.github.watching1981.common.repo.IRepoAd
import com.github.watching1981.repo.inmemory.AdRepoInMemory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties

@Suppress("unused")
@EnableConfigurationProperties(AdConfigPostgres::class)
@Configuration
class AdConfig (val postgresConfig: AdConfigPostgres){
    val logger: Logger = LoggerFactory.getLogger(AdConfig::class.java)

    @Bean
    fun processor(corSettings: MkplCorSettings) = MkplAdProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): MpLoggerProvider = MpLoggerProvider { mpLoggerLogback(it) }
    @Bean
    fun testRepo(): IRepoAd = AdRepoInMemory()

    @Bean
    fun prodRepo(): IRepoAd = RepoAdSql(postgresConfig.psql).apply {
        logger.info("Connecting to DB with ${this}")
    }

    @Bean
    fun stubRepo(): IRepoAd = AdRepoStub()

    @Bean
    fun corSettings(): MkplCorSettings = MkplCorSettings(
        loggerProvider = loggerProvider(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
        repoStub = stubRepo(),
    )


    @Bean
    fun appSettings(
        corSettings: MkplCorSettings,
        processor: MkplAdProcessor,
    ) = MkplAppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
