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

fun ICorChainDsl<MkplContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbAdIdRequest(adRepoPrepare)
        when(val result = adRepo.deleteAd(request)) {
            is DbAdResponseOk -> adRepoDone = result.data
            is DbAdResponseErr -> {
                fail(result.errors)
                adRepoDone = adRepoRead
            }
            is DbAdResponseErrWithData -> {
                fail(result.errors)
                adRepoDone = result.data
            }
        }
    }
}
