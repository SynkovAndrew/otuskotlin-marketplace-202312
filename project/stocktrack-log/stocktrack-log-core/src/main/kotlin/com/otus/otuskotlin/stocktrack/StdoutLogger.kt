package com.otus.otuskotlin.stocktrack

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object StdoutLogger: LoggerWrapper {
    override val loggerId: String = this::class.java.simpleName

    override fun log(
        level: LogLevel,
        message: String,
        throwable: Throwable?,
        data: Any?,
        arguments: Map<String, Any>,
        marker: String
    ) {
        val logParts = listOfNotNull(
            "${nowUtc()}",
            level.name,
            "$marker: $message",
            throwable?.let { "${it.message}:\n${it.stackTraceToString()}" },
            data.toString(),
            arguments.toString(),
        )
        println(logParts.joinToString("\n"))
    }

    override fun close() {
    }

    private fun nowUtc():LocalDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"))
}