package com.github.watching1981.biz.validation

import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.errorValidation
import com.github.watching1981.common.helpers.fail

fun ICorChainDsl<MkplContext>.validateIdIsPositive(title: String) = worker {
    this.title = title
    on { adValidating.id.asLong() <= 0 }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "negative",
                description = "field must not be negative"
            )
        )
    }
}
