package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.configuration.KtorApplicationSettings
import com.otus.otuskotlin.stocktrack.context.GetStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.context.PostStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.plugins.configureAuthentication
import com.otus.otuskotlin.stocktrack.plugins.configureSerialization
import com.otus.otuskotlin.stocktrack.plugins.configureWeb
import com.otus.otuskotlin.stocktrack.plugins.routing.configureStockRoutes
import com.otus.otuskotlin.stocktrack.snapshot.StockSnapshot
import com.otus.otuskotlin.stocktrack.snapshot.StockSnapshotRepository
import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.stock.StockRepository
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StubStockRepository
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import kotlin.random.Random
import kotlin.time.Duration

fun main() {
    embeddedServer(Netty, port = 8090, host = "0.0.0.0", module = Application::modules)
        .start(wait = true)
}

fun Application.modules() {
    val postgreSqlProperties = PostgreSqlProperties()
    val loggerProvider = LoggerProvider { logbackLoggerWrapper(it) }
    val cacheStockRepository = CacheStockRepository()
    val postgreSqlStockRepository = PostgreSqlStockRepository(properties = postgreSqlProperties)
    val postgreSqlSnapshotRepository = PostgreSqlSnapshotRepository(properties = postgreSqlProperties)
    val coreSettings = CoreSettings(
        loggerProvider = loggerProvider,
        prodStockRepository = postgreSqlStockRepository,
        testStockRepository = cacheStockRepository,
        stubStockRepository = StubStockRepository(),
        stockSnapshotRepository = postgreSqlSnapshotRepository
    )
    val singleStockResponseProcessor = SingleStockResponseProcessor(coreSettings = coreSettings)
    val searchStocksResponseProcessor = SearchStocksResponseProcessor(coreSettings = coreSettings)
    val getStockSnapshotsProcessor = GetStockSnapshotsProcessor(coreSettings = coreSettings)
    val postStockSnapshotsProcessor = PostStockSnapshotsProcessor(coreSettings = coreSettings)
    val applicationSettings = KtorApplicationSettings(
        coreSettings = coreSettings,
        processors = mapOf(
            SingleStockResponseContext::class to singleStockResponseProcessor,
            SearchStocksResponseContext::class to searchStocksResponseProcessor,
            GetStockSnapshotsContext::class to getStockSnapshotsProcessor,
            PostStockSnapshotsContext::class to postStockSnapshotsProcessor,
        )
    )

    configureAuthentication()
    configureSerialization()
    configureStockRoutes(applicationSettings)
    configureWeb()
}
