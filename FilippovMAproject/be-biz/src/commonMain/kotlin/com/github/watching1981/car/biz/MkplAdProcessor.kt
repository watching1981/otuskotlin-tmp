package com.github.watching1981.car.biz
import com.github.watching1981.biz.general.initStatus
import com.github.watching1981.biz.general.operation
import com.github.watching1981.biz.general.stubs
import com.github.watching1981.biz.repo.*
import com.github.watching1981.biz.stubs.*
import com.github.watching1981.biz.validation.*
import com.github.watching1981.common.MkplCorSettings
import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.models.*
import com.github.watching1981.cor.chain
import com.github.watching1981.cor.rootChain
import com.github.watching1981.cor.worker
import repo.checkLock

//@Suppress("unused", "RedundantSuspendModifier")
class MkplAdProcessor(private val corSettings: MkplCorSettings = MkplCorSettings.NONE) {
  suspend fun exec(ctx: MkplContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })
      private val businessChain = rootChain<MkplContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")
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
                worker("Очистка локации") {  adValidating.location = adValidating.location.trim() }
                worker("Очистка телефона") { adValidating.contactPhone = adValidating.contactPhone.trim() }
                validateTitleNotEmpty("Проверка, что заголовок не пуст")
                validateTitleHasContent("Проверка символов в заголовке")
                validateDescriptionNotEmpty("Проверка, что описание не пусто")
                validateDescriptionHasContent("Проверка символов в описании")
                validatePriceNotEmpty("Проверка, что цена указана и положительна")
                validatePriceRange("Проверка диапазона цены", min = 0.0, max = 1_000_000_000.0)

                finishAdValidation("Завершение проверок")
            }
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание объявления в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Получить объявление", MkplCommand.GET) {
            stubs("Обработка стабов") {
                stubGetSuccess("Имитация успешной обработки", corSettings)
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
            chain {
                title = "Логика чтения"
                repoRead("Чтение объявления из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == MkplState.RUNNING }
                    handle { adRepoDone = adRepoRead }
                }
            }
            prepareResult("Подготовка ответа")
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
                validatePriceNotEmpty("Проверка, что цена указана и положительна")
                validatePriceRange("Проверка диапазона цены", min = 0.0, max = 1_000_000_000.0)


                finishAdValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика сохранения"
                repoRead("Чтение объявления из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление объявления в БД")
            }
            prepareResult("Подготовка ответа")

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
            chain {
                title = "Логика удаления"
                repoRead("Чтение объявления из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление объявления из БД")
            }
            prepareResult("Подготовка ответа")
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
                worker("Очистка названия брэнда") { adFilterValidating.filters.brand = adFilterValidating.filters.brand?.trim() }
                worker("Очистка названия модели") { adFilterValidating.filters.model = adFilterValidating.filters.model?.trim() }
                validateBrandLength("Валидация брэнда в строке поиска в фильтре")
                validateYearRange("Валидация диапазона года выпуска")
                validatePriceRange("Проверка диапазона цены", min = 0.0, max = 1_000_000_000.0)

                finishAdFilterValidation("Успешное завершение процедуры валидации")
            }
            repoSearch("Поиск объявления в БД по фильтру")
            prepareResult("Подготовка ответа")
        }

    }.build()

}
