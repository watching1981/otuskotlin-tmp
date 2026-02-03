package com.github.watching1981.biz.exceptions

import com.github.watching1981.common.models.MkplWorkMode

class MkplAdDbNotConfiguredException(val workMode: MkplWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)
