package com.github.watching1981.biz.general

import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.MkplState

fun ICorChainDsl<MkplContext>.initStatus(title: String) = worker() {
    this.title = title
    this.description = """
        Этот обработчик устанавливает стартовый статус обработки. 
    """.trimIndent()
    on { state == MkplState.NONE }
    handle { state = MkplState.RUNNING }
}
