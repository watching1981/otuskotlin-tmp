package com.github.watching1981.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MkplUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MkplUserId("")
    }
}
