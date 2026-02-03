package com.github.watching1981.biz.repo

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.common.models.MkplUserId
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker

fun ICorChainDsl<MkplContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == MkplState.RUNNING }
    handle {
        adRepoPrepare = adValidated.copy()
        // TODO будет реализовано в занятии по управлению пользвателями
        adRepoPrepare.authorId = MkplUserId.NONE
    }
}
