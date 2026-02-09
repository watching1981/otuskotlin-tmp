package com.github.watching1981.biz.validation

import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker
import com.github.watching1981.common.helpers.errorValidation
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.fail


fun ICorChainDsl<MkplContext>.validatePriceNotEmpty(title: String) = worker {
    this.title = title
    on { adValidating.price<=0 }
    handle {
        fail(
            errorValidation(
                field = "price",
                violationCode = "empty",
                description = "Price must be specified"
            )
        )
    }
}
