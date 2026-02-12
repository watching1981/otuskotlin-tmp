package com.github.watching1981.common.repo.exceptions

import com.github.watching1981.common.models.MkplAdvertisementId
import com.github.watching1981.common.models.MkplAdLock

class RepoConcurrencyException(id: MkplAdvertisementId, expectedLock: MkplAdLock, actualLock: MkplAdLock?) : RepoAdException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)