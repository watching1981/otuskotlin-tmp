package com.github.watching1981.common.models

import com.github.watching1981.car.logging.common.LogLevel

data class MkplError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level:LogLevel =LogLevel.ERROR,
    val exception: Throwable? = null,
)
