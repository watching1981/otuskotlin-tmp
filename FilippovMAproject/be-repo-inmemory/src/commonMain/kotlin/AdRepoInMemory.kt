package com.github.watching1981.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.github.watching1981.common.models.*
import com.github.watching1981.common.repo.*
import com.github.watching1981.common.repo.exceptions.RepoEmptyLockException
import com.github.watching1981.repo.common.IRepoAdInitializable
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes


class AdRepoInMemory(
    ttl: Duration = 5.minutes,

    //val randomUuid: () -> Long = {Random.nextLong(1, Long.MAX_VALUE)},
    val randomUuid: () -> Long = {Random.nextLong(1, 1000000000L)},
    val randomLockUuid: () -> String = { uuid4().toString() },
) : AdRepoBase(), IRepoAd, IRepoAdInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, AdEntity>()
        .expireAfterWrite(ttl)
        .build()


    override fun save(ads: Collection<MkplAdvertisement>) = ads.map { ad ->
        val entity = AdEntity(ad)
        require(entity.id != null)
        cache.put(entity.id.toString(), entity)
        val allEntries = cache.asMap()
        println("Cache trying to save=====================================")
        allEntries.forEach { (key, value) ->
            println("Key: $key, Value: $value")
        }
        ad
    }

    override suspend fun createAd(rq: DbAdRequest): IDbAdResponse = tryAdMethod {
        val key = randomUuid()
        val ad = rq.ad.copy(id = MkplAdvertisementId(key),lock = MkplAdLock(randomLockUuid()))
        val entity = AdEntity(ad)
        mutex.withLock {
            cache.put(key.toString(), entity)
        }
        DbAdResponseOk(ad)
    }



    override suspend fun readAd(rq: DbAdIdRequest): IDbAdResponse = tryAdMethod {
        val key = rq.id.takeIf { it != MkplAdvertisementId.NONE }?.asLong().toString() ?: return@tryAdMethod errorEmptyId
        mutex.withLock {
            val allEntries = cache.asMap()
            println("Cache to read=====================================")
            allEntries.forEach { (key, value) ->
                println("Key: $key, Value: $value")
            }
            cache.get(key)
                ?.let {
                    DbAdResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)


        }
    }



    override suspend fun updateAd(rq: DbAdRequest): IDbAdResponse = tryAdMethod {
        val rqAd = rq.ad
        val id = rqAd.id.takeIf { it != MkplAdvertisementId.NONE } ?: return@tryAdMethod errorEmptyId
        val key = id.asLong().toString()
        val oldLock = rqAd.lock.takeIf { it != MkplAdLock.NONE } ?: return@tryAdMethod errorEmptyLock(id)

        mutex.withLock {
            val allEntries = cache.asMap()
            println("Cache to update=====================================")
            allEntries.forEach { (key, value) ->
                println("Key: $key, Value: $value")
            }
            val oldAd = cache.get(key)?.toInternal()
            when {
                oldAd == null -> errorNotFound(id)
                oldAd.lock == MkplAdLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldAd.lock != oldLock -> errorRepoConcurrency(oldAd, oldLock)
                else -> {
                    val newAd = rqAd.copy(lock = MkplAdLock(randomLockUuid()))
                    val entity = AdEntity(newAd)
                    cache.put(key, entity)
                    DbAdResponseOk(newAd)
                }
            }
        }
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): IDbAdResponse = tryAdMethod {
        val id = rq.id.takeIf { it != MkplAdvertisementId.NONE } ?: return@tryAdMethod errorEmptyId
        val key = id.asLong().toString()
        val oldLock = rq.lock.takeIf { it != MkplAdLock.NONE } ?: return@tryAdMethod errorEmptyLock(id)
        //val oldLock = rq.lock.takeIf { it != MkplAdLock.NONE } ?: MkplAdLock("124-354")
        mutex.withLock {
            val oldAd = cache.get(key)?.toInternal()
            when {
                oldAd == null -> errorNotFound(id)
                oldAd.lock == MkplAdLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldAd.lock != oldLock -> errorRepoConcurrency(oldAd, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbAdResponseOk(oldAd)
                }
            }
        }
    }

    /**
     * Поиск объявлений по фильтру
     */

    override suspend fun searchAd(rq: DbAdFilterRequest): IDbAdsResponse = tryAdsMethod {
        // Получаем все объявления из кэша
        val allAds = cache.asMap().values.map { it.toInternal() }.toList()

        // Применяем фильтры
        val filtered = allAds.filter { ad ->
            // 1. Фильтр по заголовку
              (rq.titleFilter.isBlank() ||
              ad.title.contains(rq.titleFilter, ignoreCase = true)) &&
             //  Фильтр по описанию
             (rq.descriptionFilter.isBlank() ||
              ad.description.contains(rq.descriptionFilter, ignoreCase = true)) &&
             //  Фильтр по автору
             (rq.authorId == MkplUserId.NONE || ad.authorId == rq.authorId) &&
              rq.minYear?.let { ad.car.year >= it } ?: true &&
              rq.maxYear?.let { ad.car.year <= it } ?: true &&
              rq.minPrice?.let { ad.price >= it } ?: true &&
              rq.maxPrice?.let { ad.price <= it } ?: true &&
              (rq.brandFilter.isBlank() ||
              ad.car.brand.contains(rq.brandFilter, ignoreCase = true)) &&
              (rq.modelFilter.isBlank() ||
                            ad.car.model.contains(rq.modelFilter, ignoreCase = true))
        }

        DbAdsResponseOk(filtered)
    }

}
