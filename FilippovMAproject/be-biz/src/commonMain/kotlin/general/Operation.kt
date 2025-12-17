package com.github.watching1981.biz.general

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.MkplCommand
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.chain

fun ICorChainDsl<MkplContext>.operation(
    title: String,
    command: MkplCommand,
    block: ICorChainDsl<MkplContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == MkplState.RUNNING }
}
