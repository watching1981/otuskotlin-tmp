package com.github.watching1981.biz.validation

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker

fun ICorChainDsl<MkplContext>.finishAdValidation(title: String) = worker {
    this.title = title
    on { state == MkplState.RUNNING }
    handle {
        adValidated = adValidating
    }
}

fun ICorChainDsl<MkplContext>.finishAdFilterValidation(title: String) = worker {
    this.title = title
    on { state == MkplState.RUNNING }
    handle {
        adFilterValidated = adFilterValidating
    }
}
