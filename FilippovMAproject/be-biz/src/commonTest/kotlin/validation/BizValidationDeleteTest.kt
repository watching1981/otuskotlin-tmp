package com.github.watching1981.biz.validation

import com.github.watching1981.common.models.MkplCommand
import validation.validationLockCorrect
import validation.validationLockEmpty
import validation.validationLockFormat
import validation.validationLockTrim
import kotlin.test.Test

class BizValidationDeleteTest: BaseBizValidationTest() {
    override val command = MkplCommand.DELETE

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun emptyId() = validationPositive(command, processor)
    @Test fun badRangeId() = validationRange(command, processor)

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)

}
