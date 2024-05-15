package com.otus.otuskotlin.stocktrack

import kotlin.time.measureTimedValue

interface LoggerWrapper : AutoCloseable {
    val loggerId: String

    fun log(
        level: LogLevel,
        message: String,
        throwable: Throwable? = null,
        data: Any? = null,
        arguments: Map<String, Any> = emptyMap(),
        marker: String = "DEV",
    )

    fun error(
        message: String,
        throwable: Throwable? = null,
        data: Any? = null,
        arguments: Map<String, Any> = emptyMap()
    ) {
        log(LogLevel.ERROR, message, throwable, data, arguments)
    }

    fun info(
        message: String,
        data: Any? = null,
        arguments: Map<String, Any> = emptyMap()
    ) {
        log(LogLevel.INFO, message, null, data, arguments)
    }

    fun debug(
        message: String,
        data: Any? = null,
        metadata: Map<String, Any> = emptyMap()
    ) {
        log(LogLevel.DEBUG, message, null, data, metadata)
    }

    suspend fun <T> callLogged(
        id: String,
        level: LogLevel = LogLevel.INFO,
        callable: suspend () -> T,
    ): T {
        return try {
            log(level = level, message = "$loggerId $id started")
            val (result, duration) = measureTimedValue { callable() }
            log(
                level = level,
                message = "$loggerId $id finished",
                arguments = mapOf("duration" to duration.inWholeMilliseconds)
            )

            result
        } catch (throwable: Throwable) {
            error("$loggerId $id failed", throwable)
            throw throwable
        }
    }

    suspend fun <T> callErrorLogged(
        id: String,
        block: suspend () -> T,
        throwRequired: Boolean = true,
    ): T? {
        return try {
            block()
        } catch (throwable: Throwable) {
            error("Failed $loggerId $id", throwable)
            if (throwRequired) throw throwable else null
        }
    }
}