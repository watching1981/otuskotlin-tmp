package com.github.watching1981.biz.validation

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.chain

fun ICorChainDsl<MkplContext>.validation(block: ICorChainDsl<MkplContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == MkplState.RUNNING }
}
