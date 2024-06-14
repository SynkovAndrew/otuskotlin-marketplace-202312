package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockSnapshotsRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockSnapshotsResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.ResponseResult
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.api.v1.models.StockSnapshot
import com.otus.otuskotlin.stocktrack.api.v1.models.UploadStockSnapshot
import com.otus.otuskotlin.stocktrack.testModules
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.datetime.Clock
import org.assertj.core.api.Assertions.assertThat
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration

class SnapshotRoutingTest {

    @Test
    fun `find stock snapshots successfully`() {
        testApplication {
            application { testModules() }

            val stock = storeStock("TestStock", StockCategory.SHARE)
            val snapshots = listOf(
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

            uploadSnapshots(snapshots)

            val response = configuredHttpClient().post {
                url("/api/v1/snapshot/find")
                contentType(ContentType.Application.Json)
                setBody(
                    FindStockSnapshotsRequest(
                        requestType = "find_snapshots",
                        stockId = stock.id.value
                    )
                )
            }
            val findSnapshotsResponse = response.body<FindStockSnapshotsResponse>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertThat(findSnapshotsResponse)
                .usingRecursiveComparison()
                .ignoringFields("snapshots.id")
                .isEqualTo(
                    FindStockSnapshotsResponse(
                        responseType = "find_snapshots",
                        result = ResponseResult.SUCCESS,
                        errors = emptyList(),
                        snapshots = snapshots.map {
                            StockSnapshot(
                                id = UUID.randomUUID().toString(),
                                stockId = it.stockId,
                                value = it.value,
                                timestamp = it.timestamp
                            )
                        }
                    )
                )
        }
    }
}