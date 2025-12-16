package com.github.watching1981.car.biz
import com.github.watching1981.biz.general.initStatus
import com.github.watching1981.biz.general.operation
import com.github.watching1981.biz.general.stubs
import com.github.watching1981.biz.stubs.*
import com.github.watching1981.biz.validation.*
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.stubs.MkplAdStub
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import com.github.watching1981.cor.rootChain
import com.github.watching1981.cor.worker
import kotlinx.datetime.Clock

//@Suppress("unused", "RedundantSuspendModifier")
class MkplAdProcessor(private val corSettings: MkplCorSettings = MkplCorSettings.NONE) {
  suspend fun exec(ctx: MkplContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })
    private val businessChain = rootChain<MkplContext> {
        initStatus("Инициализация статуса")

        operation("Создание объявления", MkplCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adValidating") { adValidating = adRequest.copy() }
                worker("Очистка заголовка") { adValidating.title = adValidating.title.trim() }
                worker("Очистка описания") { adValidating.description = adValidating.description.trim() }
                validateTitleNotEmpty("Проверка, что заголовок не пуст")
                validateTitleHasContent("Проверка символов")
                validateDescriptionNotEmpty("Проверка, что описание не пусто")
                validateDescriptionHasContent("Проверка символов")

                finishAdValidation("Завершение проверок")
            }
        }
        operation("Получить объявление", MkplCommand.GET) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adValidating") { adValidating = adRequest.copy() }
                validateIdIsPositive("Проверка на положительный id")
                validateIdInRange("Проверка диапазона id")

                finishAdValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Изменить объявление", MkplCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adValidating") { adValidating = adRequest.copy() }
                worker("Очистка lock") { adValidating.lock = MkplAdLock(adValidating.lock.asString().trim()) }
                worker("Очистка заголовка") { adValidating.title = adValidating.title.trim() }
                worker("Очистка описания") { adValidating.description = adValidating.description.trim() }
                validateIdIsPositive("Проверка на положительный id")
                validateIdInRange("Проверка диапазона id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateTitleNotEmpty("Проверка на непустой заголовок")
                validateTitleHasContent("Проверка на наличие содержания в заголовке")
                validateDescriptionNotEmpty("Проверка на непустое описание")
                validateDescriptionHasContent("Проверка на наличие содержания в описании")

                finishAdValidation("Успешное завершение процедуры валидации")
            }

        }
        operation("Удалить объявление", MkplCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adValidating") {
                    adValidating = adRequest.copy()
                }
                worker("Очистка lock") { adValidating.lock = MkplAdLock(adValidating.lock.asString().trim()) }
                validateIdIsPositive("Проверка на положительный id")
                validateIdInRange("Проверка диапазона id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                finishAdValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Поиск объявлений", MkplCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adFilterValidating") { adFilterValidating = adFilterRequest.copy() }
                validateBrandLength("Валидация брэнда в строке поиска в фильтре")

                finishAdFilterValidation("Успешное завершение процедуры валидации")
            }
        }

    }.build()

}
