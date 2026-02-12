package com.github.watching1981.common.repo.exceptions

import com.github.watching1981.common.models.MkplAdvertisementId

class RepoEmptyLockException(id: MkplAdvertisementId) : RepoAdException(
    id,
    "Lock is empty in DB"
)