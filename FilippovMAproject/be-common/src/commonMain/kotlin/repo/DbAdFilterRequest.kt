package com.github.watching1981.common.repo

import com.github.watching1981.common.models.*


data class DbAdFilterRequest(
    // Базовые фильтры
    val titleFilter: String = "",
    val descriptionFilter: String = "",
    val authorId: MkplUserId = MkplUserId.NONE,

    // Фильтры автомобиля
    val brandFilter: String = "",
    val modelFilter: String = "",
    val minYear: Int? = null,
    val maxYear: Int? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minMileage: Int? = null,
    val maxMileage: Int? = null,
    val engineTypes: Set<MkplEngineType> = emptySet(),
    val transmissions: Set<MkplTransmission> = emptySet(),

    // Локация
    val locationFilter: String = "",

    // Статус
    val status: McplAdvertisementStatus? = null,

    // Пагинация
    val page: Int = 1,
    val pageSize: Int = 20,

    // Сортировка
    val sortField: MkplSortField = MkplSortField.CREATED_AT,
    val sortDirection: SortDirection = SortDirection.DESC
) {
    companion object {
        val NONE = DbAdFilterRequest()
    }
}

/**
 * Поля для сортировки
 */
enum class MkplSortField {
    CREATED_AT,    // Дата создания
    UPDATED_AT,    // Дата обновления
    PRICE,         // Цена
    YEAR,          // Год выпуска
    MILEAGE,       // Пробег
    TITLE          // Заголовок
}

/**
 * Направление сортировки
 */
enum class SortDirection {
    ASC,    // По возрастанию
    DESC    // По убыванию
}