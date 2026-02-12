package com.github.watching1981.app.spring.config

import com.github.watching1981.app.common.IMkplAppSettings
import com.github.watching1981.backend.repository.inmemory.AdRepoStub
import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.repo.inmemory.AdRepoInMemory

data class MkplAppSettings(
    override val corSettings: MkplCorSettings,
    override val processor: MkplAdProcessor,
): IMkplAppSettings
