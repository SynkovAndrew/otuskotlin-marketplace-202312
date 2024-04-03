package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.api.v1.models.ResponseResult
import com.otus.otuskotlin.stocktrack.api.v1.models.StockResponseBody
import com.otus.otuskotlin.stocktrack.model.Error
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.model.Stock

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

fun Error.toTransportModel(): com.otus.otuskotlin.stocktrack.api.v1.models.Error {
    return com.otus.otuskotlin.stocktrack.api.v1.models.Error(
        code = code,
        group = group,
        field = field,
        message = message
    )
}