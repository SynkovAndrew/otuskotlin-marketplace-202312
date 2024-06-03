package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.configuration.KtorApplicationSettings
import com.otus.otuskotlin.stocktrack.context.GetStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.plugins.configureAuthentication
import com.otus.otuskotlin.stocktrack.plugins.configureRouting
import com.otus.otuskotlin.stocktrack.plugins.configureSerialization
import com.otus.otuskotlin.stocktrack.plugins.configureWeb
import com.otus.otuskotlin.stocktrack.plugins.routing.configureStockRoutes
import com.otus.otuskotlin.stocktrack.stock.StubStockRepository
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8090, host = "0.0.0.0", module = Application::modules)
        .start(wait = true)
}

fun Application.modules() {
    val postgreSqlProperties = PostgreSqlProperties()
    val loggerProvider = LoggerProvider { logbackLoggerWrapper(it) }
    val cacheStockRepository = CacheStockRepository()
    val coreSettings = CoreSettings(
        loggerProvider = loggerProvider,
        prodStockRepository = PostgreSqlStockRepository(properties = postgreSqlProperties),
        testStockRepository = cacheStockRepository,
        stubStockRepository = StubStockRepository(),
        stockSnapshotRepository = PostgreSqlSnapshotRepository(properties = postgreSqlProperties)
    )
    val singleStockResponseProcessor = SingleStockResponseProcessor(coreSettings = coreSettings)
    val searchStocksResponseProcessor = SearchStocksResponseProcessor(coreSettings = coreSettings)
    val getStockSnapshotsProcessor = GetStockSnapshotsProcessor(coreSettings = coreSettings)
    val applicationSettings = KtorApplicationSettings(
        coreSettings = coreSettings,
        processors = mapOf(
            SingleStockResponseContext::class to singleStockResponseProcessor,
            SearchStocksResponseContext::class to searchStocksResponseProcessor,
            GetStockSnapshotsContext::class to getStockSnapshotsProcessor
        )
    )

    configureAuthentication()
    configureSerialization()
    configureRouting()
    configureStockRoutes(applicationSettings)
    configureWeb()
}
