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
    configureData(postgreSqlStockRepository, postgreSqlSnapshotRepository)
}

fun configureData(
    stockRepository: StockRepository,
    stockSnapshotRepository: StockSnapshotRepository
) {

    val stock1 = Stock(
        Stock.Id(UUID.randomUUID().toString()),
        "Gazprombank",
        Stock.Category.SHARE
    )
    val stock2 = Stock(
        Stock.Id(UUID.randomUUID().toString()),
        "Alphabank",
        Stock.Category.SHARE
    )
    val stock3 = Stock(
        Stock.Id(UUID.randomUUID().toString()),
        "T-bank",
        Stock.Category.BOND
    )
    val stock4 = Stock(
        Stock.Id(UUID.randomUUID().toString()),
        "Yandex Ltd.",
        Stock.Category.SHARE
    )
    val stock5 = Stock(
        Stock.Id(UUID.randomUUID().toString()),
        "Ozon Company",
        Stock.Category.SHARE
    )

    runBlocking {
        stockRepository.create(StockRepositoryRequest(stock1))
        stockRepository.create(StockRepositoryRequest(stock2))
        stockRepository.create(StockRepositoryRequest(stock3))
        stockRepository.create(StockRepositoryRequest(stock4))
        stockRepository.create(StockRepositoryRequest(stock5))
        generateDataForStock(stock1.id).forEach { stockSnapshotRepository.create(it) }
        generateDataForStock(stock2.id).forEach { stockSnapshotRepository.create(it) }
        generateDataForStock(stock3.id).forEach { stockSnapshotRepository.create(it) }
        generateDataForStock(stock4.id).forEach { stockSnapshotRepository.create(it) }
        generateDataForStock(stock5.id).forEach { stockSnapshotRepository.create(it) }
    }
}

fun generateDataForStock(stockId: Stock.Id): List<StockSnapshot> {
    val delta = 1
    val initial = StockSnapshot(
        id = StockSnapshot.Id(value = UUID.randomUUID().toString()),
        stockId = stockId,
        value =  Random.nextDouble(12.3, 99.1).toBigDecimal(),
        timestamp = kotlinx.datetime.Instant.parse("2020-12-09T09:16:56.000124Z")
    )

    return generateSequence(initial) {
        StockSnapshot(
            id = StockSnapshot.Id(value = UUID.randomUUID().toString()),
            stockId = stockId,
            value =  Random
                .nextDouble(it.value.toDouble() - delta.toDouble(), it.value.toDouble() + delta.toDouble())
                .toBigDecimal(),
            timestamp = it.timestamp.plus(Duration.parseIsoString("PT1200M")),
        )
    }.take(10000).toList()
}
