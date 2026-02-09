package com.github.watching1981.biz.stubs

import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.fail
import com.github.watching1981.common.models.MkplError
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.common.stubs.MkplStubs

fun ICorChainDsl<MkplContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки базы данных
    """.trimIndent()
    on { stubCase == MkplStubs.DB_ERROR && state == MkplState.RUNNING }
    handle {
        fail(
            MkplError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
