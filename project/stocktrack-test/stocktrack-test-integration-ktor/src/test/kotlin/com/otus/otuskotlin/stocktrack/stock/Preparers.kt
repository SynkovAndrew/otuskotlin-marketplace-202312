package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.Debug
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugMode
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugStub
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.api.v1.models.StockResponseBody
import com.otus.otuskotlin.stocktrack.api.v1.models.StockSnapshot
import com.otus.otuskotlin.stocktrack.api.v1.models.UploadStockSnapshot
import com.otus.otuskotlin.stocktrack.api.v1.models.UploadStockSnapshotsRequest
import io.ktor.client.call.*
import io.ktor.client.request.*
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