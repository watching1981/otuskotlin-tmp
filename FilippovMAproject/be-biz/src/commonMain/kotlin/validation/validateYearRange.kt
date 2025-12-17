package com.github.watching1981.biz.validation

import com.github.watching1981.common.MkplContext
import com.github.watching1981.common.helpers.errorValidation
import com.github.watching1981.common.helpers.fail
import com.github.watching1981.cor.ICorChainDsl
import com.github.watching1981.cor.worker


fun ICorChainDsl<MkplContext>.validateYearRange(
    title: String,
    minAllowedYear: Int = 1990,
    maxAllowedYear: Int = 2025
) = worker {
    this.title = title
    on {
        val filters = adFilterValidating.filters

        // Проверяем только если задан хотя бы один из параметров года
        (filters.minYear != null || filters.maxYear != null) && (
                // Если задан minYear, проверяем его валидность
                (filters.minYear != null && filters.minYear !in minAllowedYear..maxAllowedYear) ||
                        // Если задан maxYear, проверяем его валидность
                        (filters.maxYear != null && filters.maxYear !in minAllowedYear..maxAllowedYear) ||
                        // Если заданы оба, проверяем что minYear <= maxYear
                        (filters.minYear != null && filters.maxYear != null &&
                                filters.minYear!! > filters.maxYear!!)
                )
    }
    handle {
        val filters = adFilterValidating.filters
        val desc = when {
            filters.minYear != null && filters.minYear !in minAllowedYear..maxAllowedYear ->
                "Minimum year must be between $minAllowedYear and $maxAllowedYear"
            filters.maxYear != null && filters.maxYear !in minAllowedYear..maxAllowedYear ->
                "Maximum year must be between $minAllowedYear and $maxAllowedYear"
            filters.minYear != null && filters.maxYear != null &&
                    filters.minYear!! > filters.maxYear!! ->
                "Minimum year must be less than or equal to maximum year"
            else -> "Invalid year range"
        }
        fail(
            errorValidation(
                field = "yearRange",
                violationCode = "invalid-range",
                description = desc
            )
        )
    }
}