package com.github.watching1981.common.repo.exceptions

import com.github.watching1981.common.models.MkplAdvertisementId

open class RepoAdException(
    @Suppress("unused")
    val adId: MkplAdvertisementId,
    msg: String,
): RepoException(msg)
