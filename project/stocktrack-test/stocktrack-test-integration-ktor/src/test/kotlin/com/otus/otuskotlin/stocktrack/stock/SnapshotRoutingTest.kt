package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.Debug
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugMode
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugStub
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.ResponseResult
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.api.v1.models.StockResponseBody
import com.otus.otuskotlin.stocktrack.api.v1.models.StockSnapshot
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.UploadStockSnapshot
import com.otus.otuskotlin.stocktrack.api.v1.models.UploadStockSnapshotsRequest
import com.otus.otuskotlin.stocktrack.modules
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.datetime.Clock
import org.assertj.core.api.Assertions.assertThat
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration

class SnapshotRoutingTest {

    @Test
    fun `find stock snapshots successfully`() {
        testApplication {
            application { modules() }

            val stock = storeStock("TestStock", StockCategory.SHARE)
            uploadSnapshots(
                listOf(
                    UploadStockSnapshot(
                        stockId = stock.id.value,
                        value = 10.1,
                        timestamp = Clock.System.now().minus(Duration.parseIsoString("PT10S")),
                    ),
                    UploadStockSnapshot(
                        stockId = stock.id.value,
                        value = 15.1,
                        timestamp = Clock.System.now().minus(Duration.parseIsoString("PT5S")),
                    ),
                    UploadStockSnapshot(
                        stockId = stock.id.value,
                        value = 20.1,
                        timestamp = Clock.System.now().minus(Duration.parseIsoString("PT1S")),
                    )
                )
            )

        }
    }
}