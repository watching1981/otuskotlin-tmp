package com.github.watching1981.biz.validation

import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker
import com.github.watching1981.common.helpers.errorValidation
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.fail

// смотрим пример COR DSL валидации
fun ICorChainDsl<MkplContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { adValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
