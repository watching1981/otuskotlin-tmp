package com.github.watching1981.biz.validation

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.errorValidation
import com.github.watching1981.common.helpers.fail
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker

fun ICorChainDsl<MkplContext>.validatePriceRange(title: String, min: Double, max: Double) = worker {
    this.title = title
    on {
        adValidating.price !in min..max
    }
    handle {
        fail(
            errorValidation(
                field = "price",
                violationCode = "out-of-range",
                description = "Price must be between $min and $max"
            )
        )
    }
}
