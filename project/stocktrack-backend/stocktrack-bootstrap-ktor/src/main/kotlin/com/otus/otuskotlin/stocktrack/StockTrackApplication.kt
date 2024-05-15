package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.configuration.KtorApplicationSettings
import com.otus.otuskotlin.stocktrack.plugins.configureAuthentication
import com.otus.otuskotlin.stocktrack.plugins.configureRouting
import com.otus.otuskotlin.stocktrack.plugins.configureSerialization
import com.otus.otuskotlin.stocktrack.plugins.configureWeb
import com.otus.otuskotlin.stocktrack.plugins.routing.configureStockRoutes
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8090, host = "0.0.0.0", module = Application::modules)
        .start(wait = true)
}

fun Application.modules() {
    val loggerProvider = LoggerProvider { logbackLoggerWrapper(it) }
    val coreSettings = CoreSettings(loggerProvider = loggerProvider)
    val singleStockResponseProcessor = SingleStockResponseProcessor(coreSettings = coreSettings)
    val searchStocksResponseProcessor = SearchStocksResponseProcessor(coreSettings = coreSettings)
    val applicationSettings = KtorApplicationSettings(
        coreSettings = coreSettings,
        singleStockResponseProcessor = singleStockResponseProcessor,
        searchStocksResponseProcessor = searchStocksResponseProcessor
    )

    configureAuthentication()
    configureSerialization()
    configureRouting()
    configureStockRoutes(applicationSettings)
    configureWeb()
}
