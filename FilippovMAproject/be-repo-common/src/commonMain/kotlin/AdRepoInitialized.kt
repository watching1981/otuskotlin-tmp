package com.github.watching1981.repo.common

import com.github.watching1981.common.models.MkplAdvertisement

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class AdRepoInitialized(
    val repo: IRepoAdInitializable,
    initObjects: Collection<MkplAdvertisement> = emptyList(),
) : IRepoAdInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<MkplAdvertisement> = save(initObjects).toList()
}
