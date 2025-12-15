package com.github.watching1981.car.biz
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.stubs.MkplAdStub
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.MkplState
import kotlinx.datetime.Clock

@Suppress("unused", "RedundantSuspendModifier")
class MkplAdProcessor(val corSettings: MkplCorSettings) {
    suspend fun exec(ctx: MkplContext) {
        ctx.adResponse = MkplAdStub.get()
        ctx.state = MkplState.RUNNING
    }
}
