package com.github.watching1981.biz.repo

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker

fun ICorChainDsl<MkplContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == MkplState.RUNNING }
    handle {
        adRepoPrepare = adRepoRead.copy().apply {
            this.title = adValidated.title
            description = adValidated.description
            price=adValidated.price

        }
    }
}
