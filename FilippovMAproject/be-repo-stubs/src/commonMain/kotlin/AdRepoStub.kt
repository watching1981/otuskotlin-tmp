package com.github.watching1981.backend.repository.inmemory


import com.github.watching1981.common.models.McplAdvertisementStatus
import com.github.watching1981.common.repo.*
import com.github.watching1981.stubs.MkplAdStub

class AdRepoStub() : IRepoAd {
    override suspend fun createAd(rq: DbAdRequest): IDbAdResponse {
        return DbAdResponseOk(
            data = MkplAdStub.get(),
        )
    }

    override suspend fun readAd(rq: DbAdIdRequest): IDbAdResponse {
        return DbAdResponseOk(
            data = MkplAdStub.get(),
        )
    }

    override suspend fun updateAd(rq: DbAdRequest): IDbAdResponse {
        return DbAdResponseOk(
            data = MkplAdStub.get(),
        )
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): IDbAdResponse {
        return DbAdResponseOk(
            data = MkplAdStub.get(),
        )
    }

    override suspend fun searchAd(rq: DbAdFilterRequest): IDbAdsResponse {
        return DbAdsResponseOk(
            data = MkplAdStub.prepareSearchList(status = McplAdvertisementStatus.DRAFT),
        )
    }
}
