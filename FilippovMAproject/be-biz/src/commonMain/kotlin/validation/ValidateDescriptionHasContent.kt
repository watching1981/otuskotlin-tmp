package com.github.watching1981.biz.validation

import com.github.watching1981.common.helpers.errorValidation
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.fail
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker

// пример обработки ошибки в рамках бизнес-цепочки
fun ICorChainDsl<MkplContext>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { adValidating.description.isNotEmpty() && !adValidating.description.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
