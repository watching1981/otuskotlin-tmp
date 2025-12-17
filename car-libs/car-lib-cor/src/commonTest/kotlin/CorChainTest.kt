package com.github.watching1981.cor

import kotlinx.coroutines.test.runTest
import com.github.watching1981.cor.handlers.CorChain
import com.github.watching1981.cor.handlers.CorWorker
import kotlin.test.Test
import kotlin.test.assertEquals

class CorChainTest {

    @Test
    fun `chain should execute workers`() = runTest {

        val worker1 = CorWorker<TestContext>(
            title = "w1",
            blockOn = { status == TestContext.CorStatuses.NONE },
            blockHandle = { history += "w1; " }
        )
        val worker2 = CorWorker<TestContext>(
            title = "w2",
            blockOn = { status == TestContext.CorStatuses.NONE },
            blockHandle = { history += "w2; " }
        )
        val chain = CorChain(
            execs = listOf(worker1,worker2),
            title = "chain1"
        )

        val ctx = TestContext()
        chain.exec(ctx)
        assertEquals("w1; w2; ", ctx.history)
    }
}
