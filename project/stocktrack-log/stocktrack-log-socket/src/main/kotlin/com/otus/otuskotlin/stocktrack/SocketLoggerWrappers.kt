package com.otus.otuskotlin.stocktrack

import kotlin.reflect.KClass



fun socketLoggerWrapper(
    loggerId: String,
    settings: SocketLoggerSettings = SocketLoggerSettings()
): LoggerWrapper {
    return SocketLoggerWrapper(
        loggerId = loggerId,
        host = settings.host,
        port = settings.port,
        emitToStdout = settings.emitToStdout,
        bufferSize = settings.bufferSize,
        overflowPolicy = settings.overflowPolicy,
        scope = settings.scope,
    )
}

fun socketLoggerWrapper(
    cls: KClass<*>,
    settings: SocketLoggerSettings = SocketLoggerSettings()
): LoggerWrapper {
    return socketLoggerWrapper(
        loggerId = cls.qualifiedName ?: "",
        settings = settings,
    )
}