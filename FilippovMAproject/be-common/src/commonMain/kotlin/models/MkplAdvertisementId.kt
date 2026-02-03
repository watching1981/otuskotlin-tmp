package com.github.watching1981.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MkplAdvertisementId(private val id: Long) {
    fun asLong() = id

    companion object {
        val NONE = MkplAdvertisementId(0)
    }
}
