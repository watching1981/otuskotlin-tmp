package com.github.watching1981.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import com.github.watching1981.common.helpers.asMkplError
import com.github.watching1981.common.models.*
import com.github.watching1981.common.repo.*
import com.github.watching1981.common.repo.errorNotFound
import com.github.watching1981.repo.common.IRepoAdInitializable
import kotlin.random.Random

class RepoAdSql(
    properties: SqlProperties,
    private val randomLockUuid: () -> String = { uuid4().toString() },
    private val randomUuid: () -> Long = { Random.nextLong(1, Long.MAX_VALUE)},
) : IRepoAd, IRepoAdInitializable {
    private val adTable = AdTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    fun clear(): Unit = transaction(conn) {
        adTable.deleteAll()
    }
//функция saveObj используется в методе createAd. adTable - это экземпляр класса AdTable с переданными
// в него параметрами схемы и таблицы БД, взятыми из дата-класса SqlProperties
    private fun saveObj(ad: MkplAdvertisement): MkplAdvertisement = transaction(conn) {
        val res = adTable
            .insert {  //попытка вставить данные в таблицу БД
                it.to(ad, randomLockUuid, randomUuid) //функция преобразования данных для вставки в бд из формата внутренней модели в формат запроса
            }
            .resultedValues
            ?.map { adTable.from(it) }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbAdResponse): IDbAdResponse =
        transactionWrapper(block) { DbAdResponseErr(it.asMkplError()) }

    override fun save(ads: Collection<MkplAdvertisement>): Collection<MkplAdvertisement> = ads.map { saveObj(it) }
    //createAd передает в обертку transactionWrapper конструктор класса DbAdResponseOk, в который передается
    // функция saveObj с параметром содержащим объявление, переданное извне. saveObj в свою очередь работает с объектом adTable
    //
    override suspend fun createAd(rq: DbAdRequest): IDbAdResponse = transactionWrapper {
        DbAdResponseOk(saveObj(rq.ad))
    }

    private fun read(id: MkplAdvertisementId): IDbAdResponse {
        val res = adTable.selectAll().where {
            adTable.id eq id.asLong()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbAdResponseOk(adTable.from(res))
    }

    override suspend fun readAd(rq: DbAdIdRequest): IDbAdResponse = transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: MkplAdvertisementId,
        lock: MkplAdLock,
        block: (MkplAdvertisement) -> IDbAdResponse
    ): IDbAdResponse =
        transactionWrapper {
            if (id == MkplAdvertisementId.NONE) return@transactionWrapper errorEmptyId

            val current = adTable.selectAll().where { adTable.id eq id.asLong() }
                .singleOrNull()
                ?.let { adTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    override suspend fun updateAd(rq: DbAdRequest): IDbAdResponse = update(rq.ad.id, rq.ad.lock) {
        adTable.updateReturning(where = { adTable.id eq rq.ad.id.asLong() }) {
            it.to(rq.ad.copy(lock = MkplAdLock(randomLockUuid())), randomLockUuid, randomUuid)
        }.singleOrNull()
            ?.let { DbAdResponseOk(adTable.from(it)) }
            ?: errorNotFound(rq.ad.id)
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): IDbAdResponse = update(rq.id, rq.lock) {
        adTable.deleteWhere { id eq rq.id.asLong() }
        DbAdResponseOk(it)
    }

    override suspend fun searchAd(rq: DbAdFilterRequest): IDbAdsResponse =
        transactionWrapper({
            val res = adTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.authorId != MkplUserId.NONE) {
                        add(adTable.author_id eq rq.authorId.value)
                    }
                    if (rq.titleFilter.isNotBlank()) {
                        add(
                            (adTable.title like "%${rq.titleFilter}%")
                                    or (adTable.description like "%${rq.titleFilter}%")
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbAdsResponseOk(data = res.map { adTable.from(it) })
        }, {
            DbAdsResponseErr(it.asMkplError())
        })
}
