package com.github.watching1981.app.spring.repo

import com.github.watching1981.common.repo.IRepoAd
import com.github.watching1981.repo.inmemory.AdRepoInMemory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class RepoInMemoryConfig {
    @Suppress("unused")
    @Bean()
    @Primary
    fun prodRepo(): IRepoAd = AdRepoInMemory()
}
