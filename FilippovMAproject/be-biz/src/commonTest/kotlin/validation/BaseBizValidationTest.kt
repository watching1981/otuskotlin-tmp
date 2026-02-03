package com.github.watching1981.biz.validation

import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.models.MkplCommand
import com.github.watching1981.repo.common.AdRepoInitialized
import com.github.watching1981.repo.inmemory.AdRepoInMemory
import com.github.watching1981.stubs.MkplAdStub

abstract class BaseBizValidationTest {
    protected abstract val command: MkplCommand
    private val repo = AdRepoInitialized(
        repo = AdRepoInMemory(),
        initObjects = listOf(
            MkplAdStub.get(),
        ),
    )
    private val settings by lazy { MkplCorSettings(repoTest = repo) }
    protected val processor by lazy { MkplAdProcessor(settings) }
}
