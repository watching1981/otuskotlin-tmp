package com.github.watching1981.biz.validation

import com.github.watching1981.common.helpers.errorValidation
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.fail
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker

fun ICorChainDsl<MkplContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { adValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
