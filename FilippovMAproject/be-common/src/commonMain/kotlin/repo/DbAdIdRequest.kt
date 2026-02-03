package com.github.watching1981.common.repo

import com.github.watching1981.common.models.MkplAdLock
import com.github.watching1981.common.models.MkplAdvertisement
import com.github.watching1981.common.models.MkplAdvertisementId

data class DbAdIdRequest(
    val id: MkplAdvertisementId,
    val lock: MkplAdLock = MkplAdLock.NONE,
) {
    constructor(ad:MkplAdvertisement ): this(ad.id, ad.lock)
}
