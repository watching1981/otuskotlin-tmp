package com.github.watching1981.biz.stubs

import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.common.stubs.MkplStubs
import com.github.watching1981.car.logging.common.LogLevel
import com.github.watching1981.common.models.MkplAdvertisement
import com.github.watching1981.stubs.MkplAdStub

fun ICorChainDsl<MkplContext>.stubCreateSuccess(title: String, corSettings: MkplCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания объявления
    """.trimIndent()
    on { stubCase == MkplStubs.SUCCESS && state == MkplState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = MkplState.FINISHING
            val stub = MkplAdStub.prepareResult {
                adRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                adRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                adRequest.price.takeIf { it>0 }?.also { this.price = it }
            }
            adResponse = stub
        }
    }
}

