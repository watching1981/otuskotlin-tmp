package com.github.watching1981.common.helpers

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.MkplError
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.car.logging.common.LogLevel

fun Throwable.asMkplError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MkplError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
inline fun MkplContext.addError(vararg error: MkplError) = errors.addAll(error)

inline fun MkplContext.fail(error: MkplError) {
    addError(error)
    state = MkplState.FAILING
}
inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = MkplError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)
