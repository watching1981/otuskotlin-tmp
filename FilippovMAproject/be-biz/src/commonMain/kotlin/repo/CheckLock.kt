package repo

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.fail
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.common.repo.errorRepoConcurrency
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker

fun ICorChainDsl<MkplContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == MkplState.RUNNING && adValidated.lock != adRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(adRepoRead, adValidated.lock).errors)
    }
}