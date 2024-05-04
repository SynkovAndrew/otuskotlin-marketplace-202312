package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State
import java.time.Instant
import kotlin.reflect.KClass

suspend fun ApplicationSettings.processSingleStockResponseContext(
    context: SingleStockResponseContext,
    kClass: KClass<*>
): SingleStockResponseContext {
    val logger: LoggerWrapper = coreSettings.loggerProvider.logger(kClass)

    return try {
        context
            .copy(startedAt = Instant.now())
            .also { logger.info("Processing command ${it.command} ...", it.toLog(it.command.name)) }
            .let { singleStockResponseProcessor.execute(it) }
            .also { logger.info("Command ${it.command} processed successfully", it.toLog(it.command.name)) }
    } catch (throwable: Throwable) {
        return context
            .also { logger.info("Command ${it.command} failed", it.toLog(it.command.name)) }
            .copy(
                state = State.FAILED,
                errors = context.errors + ErrorDescription(
                    message = throwable.message!!,
                    throwable = throwable
                )
            )
    }
}
