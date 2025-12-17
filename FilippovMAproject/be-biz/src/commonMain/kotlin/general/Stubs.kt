package com.github.watching1981.biz.general

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.common.models.MkplWorkMode
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.chain

fun ICorChainDsl<MkplContext>.stubs(title: String, block: ICorChainDsl<MkplContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == MkplWorkMode.STUB && state == MkplState.RUNNING }
    //on { state == MkplState.RUNNING }
}
