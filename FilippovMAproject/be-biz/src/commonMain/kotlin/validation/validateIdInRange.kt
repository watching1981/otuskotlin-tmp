package com.github.watching1981.biz.validation

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.errorValidation
import com.github.watching1981.common.helpers.fail
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker

fun ICorChainDsl<MkplContext>.validateIdInRange(title: String) = worker {
    this.title = title
    on {
        val idValue = adValidating.id.asLong()
        idValue > 1000000000L  // пример: не больше 1 миллиарда

    }
    handle {
        val idValue = adValidating.id.asLong()
        fail(
            errorValidation(
                field = "id",
                violationCode = "too-large",
                description = "ID is too large: $idValue"
            )
        )
    }
}
