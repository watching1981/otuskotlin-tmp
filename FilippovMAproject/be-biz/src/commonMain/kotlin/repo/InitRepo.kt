package com.github.watching1981.biz.repo

import com.github.watching1981.biz.exceptions.MkplAdDbNotConfiguredException
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.errorSystem
import com.github.watching1981.common.helpers.fail
import com.github.watching1981.common.models.MkplWorkMode
import com.github.watching1981.common.repo.IRepoAd
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker

fun ICorChainDsl<MkplContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        adRepo = when {
            workMode == MkplWorkMode.TEST -> corSettings.repoTest
            workMode == MkplWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != MkplWorkMode.STUB && adRepo == IRepoAd.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = MkplAdDbNotConfiguredException(workMode)
            )
        )
    }
}
