package com.github.watching1981.common.repo

import com.github.watching1981.common.helpers.errorSystem
import com.github.watching1981.common.models.MkplAdLock
import com.github.watching1981.common.models.MkplAdvertisement
import com.github.watching1981.common.models.MkplAdvertisementId
import com.github.watching1981.common.models.MkplError
import com.github.watching1981.common.repo.exceptions.RepoConcurrencyException
import com.github.watching1981.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: MkplAdvertisementId) = DbAdResponseErr(
    MkplError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asLong()} is not Found",
    )
)

val errorEmptyId = DbAdResponseErr(
    MkplError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)
fun errorRepoConcurrency(
    oldAd: MkplAdvertisement,
    expectedLock: MkplAdLock,
    exception: Exception = RepoConcurrencyException(
        id = oldAd.id,
        expectedLock = expectedLock,
        actualLock = oldAd.lock,
    ),
) = DbAdResponseErrWithData(
    ad = oldAd,
    err = MkplError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldAd.id.asLong()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: MkplAdvertisementId) = DbAdResponseErr(
    MkplError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Ad ${id.asLong()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbAdResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)