package com.github.watching1981.common
import com.github.watching1981.car.logging.common.MpLoggerProvider


data class MkplCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
) {
    companion object {
        val NONE = MkplCorSettings()
    }
}
