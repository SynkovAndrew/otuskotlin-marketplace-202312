package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.api.v1.models.ErrorLog
import com.otus.otuskotlin.stocktrack.api.v1.models.LogLevel
import com.otus.otuskotlin.stocktrack.api.v1.models.MultipleStockResponseLog
import com.otus.otuskotlin.stocktrack.api.v1.models.SingleStockResponseLog
import com.otus.otuskotlin.stocktrack.api.v1.models.StockFilterLog
import com.otus.otuskotlin.stocktrack.api.v1.models.StockLog
import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.model.StockFilter
import java.time.OffsetDateTime
import java.time.ZoneId

const val STOCK_TRACK_SERVICE_NAME = "stock-track-service"

fun SingleStockResponseContext.toLog(logId: String) : SingleStockResponseLog  {
    return SingleStockResponseLog(
        id = logId,
        timestamp = OffsetDateTime.now(ZoneId.of("UTC")),
        source = STOCK_TRACK_SERVICE_NAME,
        errors = errors.map { it.toErrorLog() },
        requestId = requestId.asString(),
        operation = command.toSingleStockResponseOperation(),
        requestBody = request.toLog(),
        responseBody = response.toLog()
    )
}

fun SearchStocksResponseContext.toLog(logId: String) : MultipleStockResponseLog  {
    return MultipleStockResponseLog(
        id = logId,
        timestamp = OffsetDateTime.now(ZoneId.of("UTC")),
        source = STOCK_TRACK_SERVICE_NAME,
        errors = errors.map { it.toErrorLog() },
        requestId = requestId.asString(),
        operation = command.toMultipleStockResponseOperation(),
        requestBody = request.toLog(),
        responseBody = response.map { it.toLog() }
    )
}

private fun Stock.toLog() : StockLog {
    return StockLog(
        id = id.value,
        name = name,
        category = category.name
    )
}

private fun StockFilter.toLog() : StockFilterLog {
    return StockFilterLog(
        searchString = searchString ?: ""
    )
}

private fun ErrorDescription.toErrorLog() : ErrorLog {
    return ErrorLog(
        code = code,
        message = message,
        level = LogLevel.ERROR,
        field = field
    )
}

private fun Command.toSingleStockResponseOperation() :SingleStockResponseLog.Operation {
    return when(this) {
        Command.CREATE -> SingleStockResponseLog.Operation.CREATE
        Command.UPDATE -> SingleStockResponseLog.Operation.UPDATE
        Command.DELETE -> SingleStockResponseLog.Operation.DELETE
        Command.FIND -> SingleStockResponseLog.Operation.FIND
        else -> error("SingleStockResponseLog.Operation doesn't support $this command")
    }
}

private fun Command.toMultipleStockResponseOperation() : MultipleStockResponseLog.Operation {
    return when(this) {
        Command.SEARCH -> MultipleStockResponseLog.Operation.SEARCH
        else -> error("MultipleStockResponseLog.Operation doesn't support $this command")
    }
}


