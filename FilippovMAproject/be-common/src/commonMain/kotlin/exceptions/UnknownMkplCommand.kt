package com.github.watching1981.common.exceptions

import com.github.watching1981.common.models.MkplEngineType


class UnknownMkplCommand(command: MkplEngineType) : Throwable("Wrong command $command at mapping toTransport stage")
