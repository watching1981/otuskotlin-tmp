package com.github.watching1981.biz.repo

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.fail
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.common.repo.DbAdIdRequest
import com.github.watching1981.common.repo.DbAdResponseErr
import com.github.watching1981.common.repo.DbAdResponseErrWithData
import com.github.watching1981.common.repo.DbAdResponseOk
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker

fun ICorChainDsl<MkplContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbAdIdRequest(adValidated)
        when(val result = adRepo.readAd(request)) {  //обращаемся к методу readAd соответствующего репозитория
            is DbAdResponseOk -> adRepoRead = result.data
            is DbAdResponseErr -> fail(result.errors)
            is DbAdResponseErrWithData -> {
                fail(result.errors)
                adRepoRead = result.data
            }
        }
    }
}
