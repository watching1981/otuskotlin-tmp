package com.github.watching1981.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.car.logging.common.MpLoggerProvider
import com.github.watching1981.logging.jvm.mpLoggerLogback

@Suppress("unused")
@Configuration
class AdConfig {
    @Bean
    fun processor(corSettings: MkplCorSettings) = MkplAdProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): MpLoggerProvider = MpLoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun corSettings(): MkplCorSettings = MkplCorSettings(
        loggerProvider = loggerProvider(),
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
