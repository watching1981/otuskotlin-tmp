package com.github.watching1981.cor.handlers

import com.github.watching1981.cor.ICorExec
import com.github.watching1981.cor.ICorExecDsl

abstract class AbstractCorExec<T>(
    override val title: String,
    override val description: String = "",
    private val blockOn: suspend T.() -> Boolean = { true },
    private val blockExcept: suspend T.(Throwable) -> Unit = {}
) : ICorExec<T> {

    protected abstract suspend fun handle(context: T)

    override suspend fun exec(context: T) {
        if (context.blockOn()) {
            try {
                handle(context)
            } catch (e: Throwable) {
                context.blockExcept(e)
            }
        }
    }
}

abstract class CorExecDsl<T> : ICorExecDsl<T> {
    protected var blockOn: suspend T.() -> Boolean = { true }
    protected var blockExcept: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e }

    override var title: String = ""
    override var description: String = ""

    override fun on(function: suspend T.() -> Boolean) {
        blockOn = function
    }

    override fun except(function: suspend T.(e: Throwable) -> Unit) {
        blockExcept = function
    }
}
