package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.Request
import com.otus.otuskotlin.stocktrack.api.v1.models.Response
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockBody
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockResponse
import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.debug.DebugMapper
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.model.StockLock

fun Request.fromTransportModel(): Context<*, *> {
    return when (this) {
        is CreateStockRequest -> SingleStockResponseContext(
            command = Command.CREATE,
            request = body.fromTransportModel(),
            debug = DebugMapper.fromTransportModel(debug)
        )

        is UpdateStockRequest -> SingleStockResponseContext(
            command = Command.UPDATE,
            request = body.fromTransportModel(),
            debug = DebugMapper.fromTransportModel(debug)
        )

        is DeleteStockRequest -> SingleStockResponseContext(
            command = Command.DELETE,
            request = body.fromTransportModel(),
            debug = DebugMapper.fromTransportModel(debug)
        )

        is FindStockRequest -> SingleStockResponseContext(
            command = Command.FIND,
            request = body.fromTransportModel(),
            debug = DebugMapper.fromTransportModel(debug)
        )

        is SearchStocksRequest -> SearchStocksResponseContext(
            command = Command.SEARCH,
            request = filter.fromTransportModel(),
            debug = DebugMapper.fromTransportModel(debug)
        )

        else -> throw IllegalArgumentException(
            "${this::class.simpleName} is not supported by SingleStockResponseContext"
        )
    }
}

fun SingleStockResponseContext.toTransportModel(): Response {
    return when (command) {
        Command.CREATE -> CreateStockResponse(
            responseType = command.value,
            result = state.toTransportModel(),
            body = response.toTransportModel(),
            errors = errors.map { it.toTransportModel() }
        )

        Command.UPDATE -> UpdateStockResponse(
            responseType = command.value,
            result = state.toTransportModel(),
            body = response.toTransportModel(),
            errors = errors.map { it.toTransportModel() }
        )

        Command.DELETE -> DeleteStockResponse(
            responseType = command.value,
            result = state.toTransportModel(),
            body = response.toTransportModel(),
            errors = errors.map { it.toTransportModel() }
        )

        Command.FIND -> FindStockResponse(
            responseType = command.value,
            result = state.toTransportModel(),
            body = response.toTransportModel(),
            errors = errors.map { it.toTransportModel() }
        )

        else -> throw IllegalArgumentException("Search command is handled with MultipleStockResponseContext")
    }
}

fun CreateStockBody.fromTransportModel(): Stock {
    return Stock(
        name = name,
        category = StockCategoryMapper.fromTransportModel(category),
    )
}

fun DeleteStockBody.fromTransportModel(): Stock {
    return Stock(
        id = StockIdMapper.fromTransportModel(id),
        lock = StockLock(value = lock)
    )
}

fun FindStockBody.fromTransportModel(): Stock {
    return Stock(
        id = StockIdMapper.fromTransportModel(id)
    )
}

fun UpdateStockBody.fromTransportModel(): Stock {
    return Stock(
        id = StockIdMapper.fromTransportModel(id),
        name = name,
        category = StockCategoryMapper.fromTransportModel(category),
        lock = StockLock(value = lock)
    )
}