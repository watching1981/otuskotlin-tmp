package com.github.watching1981.cor.handlers

import com.github.watching1981.cor.CorDslMarker
import com.github.watching1981.cor.ICorExec
import com.github.watching1981.cor.ICorWorkerDsl

/**
 * Реализация воркера (worker)
 */
class CorWorker<T>(
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    private val blockHandle: suspend T.() -> Unit = {},
    blockExcept: suspend T.(Throwable) -> Unit = {}
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {

    override suspend fun handle(context: T) {
        blockHandle(context)
    }
}

@CorDslMarker
class CorWorkerDsl<T> : CorExecDsl<T>(), ICorWorkerDsl<T> {

    private var blockHandle: suspend T.() -> Unit = {}

    override fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun build(): ICorExec<T> = CorWorker<T>(
        title = title,
        description = description,
        blockOn = blockOn,
        blockHandle = blockHandle,
        blockExcept = blockExcept,
    )
}
