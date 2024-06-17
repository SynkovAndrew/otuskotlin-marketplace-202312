package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.context.GetStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.context.PostStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.model.ErrorDescription

class CommandBus(private val settings: ApplicationSettings) {
    private val logger: LoggerWrapper = settings.coreSettings.loggerProvider.logger(this::class)

    suspend fun <T : Context<*, *, T>> processContext(context: T): T {
        return try {
            context
                .also { log("Processing command ${it.command} ...", it) }
                .let { settings.processors[it::class] }
                ?.let { it as ResponseProcessor<T> }
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

    private fun log(message: String, context: Context<*, *, *>) {
        when (context) {
            is SearchStocksResponseContext -> logger.info(message, context.toLog(context.command.name))
            is SingleStockResponseContext -> logger.info(message, context.toLog(context.command.name))
            is GetStockSnapshotsContext -> logger.info(message, context.toLog(context.command.name))
            is PostStockSnapshotsContext -> logger.info(message, context.toLog(context.command.name))
        }
    }
}
