package com.github.watching1981.biz.stubs

import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.models.*
import com.github.watching1981.common.stubs.MkplStubs
import com.github.watching1981.car.logging.common.LogLevel
import com.github.watching1981.stubs.MkplAdStub

fun ICorChainDsl<MkplContext>.stubSearchSuccess(title: String, corSettings: MkplCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для поиска объявлений
    """.trimIndent()
    on { stubCase == MkplStubs.SUCCESS && state == MkplState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubSearchSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = MkplState.FINISHING
            adsResponse.clear()
            // Простой поиск по бренду
            val brand = adFilterRequest.filters.brand.takeIf { it?.isNotBlank() ?: false }
            val results = MkplAdStub.prepareSearchList(brand = brand)
            adsResponse.addAll(results)
        }
    }
}

