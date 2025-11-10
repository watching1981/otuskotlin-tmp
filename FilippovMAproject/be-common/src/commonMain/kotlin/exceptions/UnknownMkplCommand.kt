package com.github.watching1981.common.exceptions

import com.github.watching1981.common.models.MkplCommand


class UnknownMkplCommand(command: MkplCommand) : Throwable("Wrong command $command at mapping toTransport stage")
