package com.github.watching1981.biz.stubs

import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.models.McplAdvertisementId
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.common.stubs.MkplStubs
import com.github.watching1981.car.logging.common.LogLevel
import com.github.watching1981.stubs.MkplAdStub

fun ICorChainDsl<MkplContext>.stubUpdateSuccess(title: String, corSettings: MkplCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для изменения объявления
    """.trimIndent()
    on { stubCase == MkplStubs.SUCCESS && state == MkplState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubUpdateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = MkplState.FINISHING
            val stub = MkplAdStub.prepareResult {
                adRequest.id.takeIf { it != McplAdvertisementId.NONE }?.also { this.id = it }
                adRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                adRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            }
            adResponse = stub
        }
    }
}
