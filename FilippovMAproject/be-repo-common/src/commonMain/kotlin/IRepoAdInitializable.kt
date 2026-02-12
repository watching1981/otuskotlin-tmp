package com.github.watching1981.repo.common

import com.github.watching1981.common.models.MkplAdvertisement
import com.github.watching1981.common.repo.IRepoAd

interface IRepoAdInitializable: IRepoAd {
    fun save(ads: Collection<MkplAdvertisement>) : Collection<MkplAdvertisement>
}
