package com.otus.otuskotlin.stocktrack.exception

import com.otus.otuskotlin.stocktrack.model.Command

class CommandNotSupportedException(command: Command) : RuntimeException(
    message = "Command ${command.name} is not supported"
)
