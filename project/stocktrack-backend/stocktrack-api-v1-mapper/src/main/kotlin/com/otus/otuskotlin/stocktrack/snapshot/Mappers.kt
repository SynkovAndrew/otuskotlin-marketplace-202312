package com.otus.otuskotlin.stocktrack.snapshot

import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockSnapshotsRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.PredictStockSnapshotsRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.StockSnapshot
import com.otus.otuskotlin.stocktrack.api.v1.models.UploadStockSnapshot
import com.otus.otuskotlin.stocktrack.api.v1.models.UploadStockSnapshotsRequest
import com.otus.otuskotlin.stocktrack.context.GetStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.context.PostStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.snapshot.StockSnapshot as InternalStockSnapshot

fun UploadStockSnapshot.fromTransportModel(): InternalStockSnapshot {
    return InternalStockSnapshot(
        id = InternalStockSnapshot.Id.NONE,
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

fun PredictStockSnapshotsRequest.fromTransportModel(): GetStockSnapshotsContext {
    return GetStockSnapshotsContext(
        command = Command.PREDICT_SNAPSHOTS,
        request = Stock.Id(value = stockId)
    )
}

fun FindStockSnapshotsRequest.fromTransportModel(): GetStockSnapshotsContext {
    return GetStockSnapshotsContext(
        command = Command.FIND_SNAPSHOTS,
        request = Stock.Id(value = stockId)
    )
}

fun UploadStockSnapshotsRequest.fromTransportModel(): PostStockSnapshotsContext {
    return PostStockSnapshotsContext(
        command = Command.UPLOAD_SNAPSHOTS,
        request = snapshots.map { it.fromTransportModel() }
    )
}