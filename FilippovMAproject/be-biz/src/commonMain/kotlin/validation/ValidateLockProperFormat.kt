package com.github.watching1981.biz.validation

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.errorValidation
import com.github.watching1981.common.helpers.fail
import com.github.watching1981.common.models.MkplAdLock
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker

fun ICorChainDsl<MkplContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в MkplAdId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { adValidating.lock != MkplAdLock.NONE && !adValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = adValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}
