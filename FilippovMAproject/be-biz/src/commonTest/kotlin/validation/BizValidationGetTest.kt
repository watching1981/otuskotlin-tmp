package com.github.watching1981.biz.validation

import com.github.watching1981.common.models.MkplCommand
import kotlin.test.Test

class BizValidationGetTest: BaseBizValidationTest() {
    override val command = MkplCommand.GET

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun emptyId() = validationPositive(command, processor)
    @Test fun badFormatId() = validationIdRange(command, processor)

}
