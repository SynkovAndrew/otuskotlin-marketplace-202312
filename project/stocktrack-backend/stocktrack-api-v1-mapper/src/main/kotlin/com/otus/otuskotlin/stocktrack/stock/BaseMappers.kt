package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.api.v1.models.Response
import com.otus.otuskotlin.stocktrack.api.v1.models.ResponseResult
import com.otus.otuskotlin.stocktrack.api.v1.models.StockResponseBody
import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.context.GetStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.context.PostStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State

fun Stock.toTransportModel(): StockResponseBody {
    return StockResponseBody(
        id = StockIdMapper.toTransportModel(id),
        name = name,
        category = StockCategoryMapper.toTransportModel(category),
        lock = lock.value,
        permissions = StockPermissionMapper.toTransportModels(permissions).toSet(),
    )
}

fun State.toTransportModel(): ResponseResult {
    return when (this) {
        State.RUNNING, State.FINISHED -> ResponseResult.SUCCESS
        State.FAILED -> ResponseResult.ERROR
        State.NONE -> throw IllegalArgumentException(
            "There is no such a value $this in ResponseResult"
        )
    }
}

fun ErrorDescription.toTransportModel(): com.otus.otuskotlin.stocktrack.api.v1.models.Error {
    return com.otus.otuskotlin.stocktrack.api.v1.models.Error(
        code = code,
        group = group,
        field = field,
        message = message
    )
}

fun <T : Context<*, *, T>> Context<*, *, T>.toTransportModel() : Response {
    return when(this) {
        is SearchStocksResponseContext -> this.toTransportModel()
        is SingleStockResponseContext -> this.toTransportModel()
        is GetStockSnapshotsContext -> this.toTransportModel()
        is PostStockSnapshotsContext -> this.toTransportModel()
        else -> error("Not supported: $this")
    }
 }