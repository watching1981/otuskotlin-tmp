package com.github.watching1981.biz.stubs

import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.common.stubs.MkplStubs
import com.github.watching1981.car.logging.common.LogLevel
import com.github.watching1981.common.models.McplAdvertisementId
import com.github.watching1981.stubs.MkplAdStub

fun ICorChainDsl<MkplContext>.stubGetSuccess(title: String, corSettings: MkplCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для чтения объявления
    """.trimIndent()
    on { stubCase == MkplStubs.SUCCESS && state == MkplState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubGetSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = MkplState.FINISHING
            val stub = MkplAdStub.prepareResult {
                adRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                adRequest.id.takeIf { it != McplAdvertisementId.NONE }?.let {
                    this.id = it
                }

            }
            adResponse = stub
        }
    }
}
