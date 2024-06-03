package com.otus.otuskotlin.stocktrack.snapshot

import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockSnapshotsRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockSnapshotsResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.PredictStockSnapshotsRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.PredictStockSnapshotsResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.StockSnapshot
import com.otus.otuskotlin.stocktrack.api.v1.models.UploadStockSnapshotsRequest
import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.snapshot.StockSnapshot as InternalStockSnapshot

fun StockSnapshot.fromTransportModel(): InternalStockSnapshot {
    return InternalStockSnapshot(
        id = InternalStockSnapshot.Id(value = id),
        stockId = Stock.Id(value = stockId),
        value = value.toBigDecimal(),
        timestamp = timestamp
    )
}

fun InternalStockSnapshot.toTransportModel(): StockSnapshot {
    return StockSnapshot(
        id = id.value,
        stockId = stockId.value,
        value = value.toDouble(),
        timestamp = timestamp
    )
}

fun PredictStockSnapshotsRequest.fromTransportModel(): Stock.Id {
    return Stock.Id(value = stockId)
}

fun FindStockSnapshotsRequest.fromTransportModel(): Stock.Id {
    return Stock.Id(value = stockId)
}

fun List<InternalStockSnapshot>.toFindTransportModel(): FindStockSnapshotsResponse {
    return FindStockSnapshotsResponse(snapshots = this.map { it.toTransportModel() })
}

fun List<InternalStockSnapshot>.toPredictTransportModel(): PredictStockSnapshotsResponse {
    return PredictStockSnapshotsResponse(snapshots = this.map { it.toTransportModel() })
}

fun UploadStockSnapshotsRequest.fromTransportModel(): List<InternalStockSnapshot> {
    return snapshots.map { it.fromTransportModel() }
}