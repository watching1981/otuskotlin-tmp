package com.github.watching1981.common.models

data class MkplUserId(val value: Long){
    companion object {
        val NONE = MkplUserId(0)
    }
}