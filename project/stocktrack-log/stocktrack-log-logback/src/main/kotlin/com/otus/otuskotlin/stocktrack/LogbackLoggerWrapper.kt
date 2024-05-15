package com.otus.otuskotlin.stocktrack

import ch.qos.logback.classic.Logger
import net.logstash.logback.argument.StructuredArguments
import org.slf4j.LoggerFactory
import org.slf4j.Marker
import org.slf4j.event.KeyValuePair
import org.slf4j.event.Level
import org.slf4j.event.LoggingEvent
import java.time.Instant
import kotlin.reflect.KClass

fun logbackLoggerWrapper(logger: Logger): LoggerWrapper {
    return LogbackLoggerWrapper(logger = logger)
}

fun logbackLoggerWrapper(clazz: KClass<*>): LoggerWrapper {
    return logbackLoggerWrapper(LoggerFactory.getLogger(clazz.java) as Logger)
}

fun logbackLoggerWrapper(loggerId: String): LoggerWrapper {
    return logbackLoggerWrapper(LoggerFactory.getLogger(loggerId) as Logger)
}

private class LogbackLoggerWrapper(
    private val logger: Logger,
    override val loggerId: String = logger.name,
) : LoggerWrapper {

    override fun log(
        level: LogLevel,
        message: String,
        throwable: Throwable?,
        data: Any?,
        arguments: Map<String, Any>,
        marker: String
    ) {
        logger.log(
            LogbackEvent(
                logger = logger,
                level = level.toSlf4jLevel(),
                message = message,
                data = data,
                arguments = arguments,
                throwable = throwable,
                marker = DefaultMarker(name = marker)
            )
        )
    }

    private fun LogLevel.toSlf4jLevel(): Level {
        return when (this) {
            LogLevel.ERROR -> Level.ERROR
            LogLevel.WARN -> Level.WARN
            LogLevel.INFO -> Level.INFO
            LogLevel.DEBUG -> Level.DEBUG
            LogLevel.TRACE -> Level.TRACE
        }
    }

    private data class LogbackEvent(
        private val logger: Logger,
        private val level: Level = Level.DEBUG,
        private val message: String,
        private val data: Any? = null,
        private val arguments: Map<String, Any> = emptyMap(),
        private val throwable: Throwable? = null,
        private val marker: Marker = DefaultMarker("DEV"),
    ) : LoggingEvent {
        override fun getThrowable(): Throwable? = throwable

        override fun getTimeStamp(): Long = Instant.now().toEpochMilli()

        override fun getThreadName(): String = Thread.currentThread().name

        override fun getMessage(): String = message

        override fun getArguments(): List<Any> = argumentArray.toList()

        override fun getArgumentArray(): Array<out Any> {
            return (data?.let { (arguments + ("data" to it)) } ?: arguments)
                .map { (key, value) -> StructuredArguments.keyValue(key, value) }
                .toTypedArray()
        }

        override fun getMarkers(): List<Marker> = listOf(marker)

        override fun getKeyValuePairs(): List<KeyValuePair> {
           return arguments
                .map { (key, value) -> KeyValuePair(key, value) }
                .toList()
        }

        override fun getLevel(): Level = level

        override fun getLoggerName(): String = logger.name
    }

    override fun close() {
    }
}
