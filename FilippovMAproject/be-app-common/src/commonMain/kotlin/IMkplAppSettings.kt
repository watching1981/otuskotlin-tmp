package com.github.watching1981.app.common

import com.github.watching1981.car.biz.MkplAdProcessor
import com.github.watching1981.common.MkplCorSettings

interface IMkplAppSettings {
    val processor: MkplAdProcessor
    val corSettings: MkplCorSettings
}
