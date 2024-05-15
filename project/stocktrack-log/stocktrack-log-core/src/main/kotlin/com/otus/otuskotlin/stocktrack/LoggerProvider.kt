package com.otus.otuskotlin.stocktrack

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class LoggerProvider(
    private val provider: (String) -> LoggerWrapper = { StdoutLogger }
) {
    fun logger(loggerId: String): LoggerWrapper = provider(loggerId)

    fun logger(clazz: KClass<*>): LoggerWrapper = provider(clazz.qualifiedName ?: clazz.simpleName ?: "")

    fun logger(function: KFunction<*>): LoggerWrapper = provider(function.name)
}
