package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.Debug
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugMode
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugStub
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksFilter
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.api.v1.models.StockId
import com.otus.otuskotlin.stocktrack.api.v1.models.StockResponseBody
import com.otus.otuskotlin.stocktrack.api.v1.models.StockSnapshot
import com.otus.otuskotlin.stocktrack.api.v1.models.UploadStockSnapshot
import com.otus.otuskotlin.stocktrack.api.v1.models.UploadStockSnapshotsRequest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*

internal suspend fun ApplicationTestBuilder.storeStock(name: String, category: StockCategory): StockResponseBody {
    return configuredHttpClient()
        .post {
            url("/api/v1/stock/create")
            contentType(ContentType.Application.Json)
            setBody(
                CreateStockRequest(
                    requestType = "create",
                    debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                    body = CreateStockBody(
                        name = name,
                        category = category
                    )
                )
            )
        }
        .body<CreateStockResponse>()
        .body
}

internal suspend fun ApplicationTestBuilder.uploadSnapshots(snapshots: List<UploadStockSnapshot>) {
    return configuredHttpClient()
        .post {
            url("/api/v1/snapshot/upload")
            contentType(ContentType.Application.Json)
            setBody(
                UploadStockSnapshotsRequest(
                    requestType = "upload_snapshots",
                    snapshots = snapshots
                )
            )
        }
        .status
        .let { }
}

internal suspend fun ApplicationTestBuilder.findAllStocks(): List<StockResponseBody> {
    return configuredHttpClient()
        .post {
            url("/api/v1/stock/search")
            contentType(ContentType.Application.Json)
            setBody(
                SearchStocksRequest(
                    requestType = "search",
                    debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                    filter = SearchStocksFilter(searchString = "")
                )
            )
        }
        .body<SearchStocksResponse>()
        .body
}

internal suspend fun ApplicationTestBuilder.deleteStock(stockId: StockId, lock: String): StockResponseBody {
    return configuredHttpClient()
        .post {
            url("/api/v1/stock/delete")
            contentType(ContentType.Application.Json)
            setBody(
                DeleteStockRequest(
                    requestType = "delete",
                    debug = Debug(mode = DebugMode.PROD, stub = DebugStub.SUCCESS),
                    body = DeleteStockBody(
                        id = stockId,
                        lock = lock
                    )
                )
            )
        }
        .body<DeleteStockResponse>()
        .body
}