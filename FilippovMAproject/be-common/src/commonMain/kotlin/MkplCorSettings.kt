package com.github.watching1981.common
import com.github.watching1981.car.logging.common.MpLoggerProvider
import com.github.watching1981.common.repo.IRepoAd


data class MkplCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
    val repoStub: IRepoAd = IRepoAd.NONE,
    val repoTest: IRepoAd = IRepoAd.NONE,
    val repoProd: IRepoAd = IRepoAd.NONE,
) {
    companion object {
        val NONE = MkplCorSettings()
    }
}
