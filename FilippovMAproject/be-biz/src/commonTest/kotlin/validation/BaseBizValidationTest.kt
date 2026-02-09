package com.github.watching1981.biz.validation

import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.models.MkplCommand

abstract class BaseBizValidationTest {
    protected abstract val command: MkplCommand
    private val settings by lazy { MkplCorSettings() }
    protected val processor by lazy { MkplAdProcessor(settings) }
}
