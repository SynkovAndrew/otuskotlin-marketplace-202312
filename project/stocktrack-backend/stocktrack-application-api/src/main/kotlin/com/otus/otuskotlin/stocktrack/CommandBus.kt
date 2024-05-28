package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.model.ErrorDescription

class CommandBus(private val settings: ApplicationSettings) {
    private val logger: LoggerWrapper = settings.coreSettings.loggerProvider.logger(this::class)

    suspend fun processContext(context: Context<*, *>): Context<*, *> {
        return try {
            context
                .also { log("Processing command ${it.command} ...", it) }
                .let { settings.processors[it::class] }
                ?.let { it as ResponseProcessor<*, *, Context<*, *>> }
                ?.execute(context)
                ?.also { log("Command ${it.command} processed successfully", it) }
                ?: context.fail(ErrorDescription(message = "Response processor not found"))
        } catch (throwable: Throwable) {
            return context
                .also { log("Command ${it.command} failed", it) }
                .fail(
                    ErrorDescription(
                        message = throwable.message!!,
                        throwable = throwable
                    )
                )
        }
    }

    private fun log(message: String, context: Context<*, *>) {
        when (context) {
            is SearchStocksResponseContext -> logger.info(message, context.toLog(context.command.name))
            is SingleStockResponseContext -> logger.info(message, context.toLog(context.command.name))
        }
    }

    suspend fun processSingleStockResponseContext(
        context: SingleStockResponseContext
    ): SingleStockResponseContext {
        return processContext(context) as SingleStockResponseContext
    }
}
