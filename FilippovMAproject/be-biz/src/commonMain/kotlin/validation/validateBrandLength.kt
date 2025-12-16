package com.github.watching1981.biz.validation

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.errorValidation
import com.github.watching1981.common.helpers.fail
import com.github.watching1981.common.models.MkplState
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.chain
import com.github.watching1981.cor.worker


fun ICorChainDsl<MkplContext>.validateBrandLength(
    title: String,
    max: Int = 30
) = chain {
    this.title = title
    this.description = """
        Валидация строки брэнда в поисковых фильтрах. Допустимые значения:
        - null - не выполняем поиск по строке
        - 3-30 - допустимая длина
    """.trimIndent()
    on { state == MkplState.RUNNING }
    worker("Обрезка пустых символов") { adFilterValidating.filters.brand = adFilterValidating.filters.brand?.trim() }
    worker {
        this.title = "Проверка длины брэнда не более 30 символа"
        this.description = this.title
            on {
                    val brand = adFilterValidating.filters.brand
                    state == MkplState.RUNNING && brand != null && brand.length > max
                }
            handle {
                fail(
                    errorValidation(
                        field = "brand",
                        violationCode = "too-long",
                        description = "Brand must be at most $max characters"
                    )
                )
            }
    }
    worker {
        this.title = "Проверка минимальной длины брэнда больше 2 символов"
        this.description = this.title
         on {
             val brand = adFilterValidating.filters.brand
             state == MkplState.RUNNING && brand != null && brand.length in (1..2)
         }
        handle {
            fail(
                errorValidation(
                    field = "brand",
                    violationCode = "tooShort",
                    description = "Search string must contain at least 3 symbols"
                )
            )
        }
    }
    worker {
        this.title = "Проверка брэнда на null"
        this.description = this.title
        on {
            val brand = adFilterValidating.filters.brand
            state == MkplState.RUNNING && brand == null
        }
        handle {
            fail(
                errorValidation(
                    field = "brand",
                    violationCode = "null",
                    description = "Search string must not be null"
                )
            )
        }
    }

}
