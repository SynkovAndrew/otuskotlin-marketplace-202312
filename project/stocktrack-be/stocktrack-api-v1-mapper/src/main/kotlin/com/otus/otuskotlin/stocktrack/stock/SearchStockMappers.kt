package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksFilter
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksResponse
import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.debug.DebugMapper
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.StockFilter

fun SearchStocksRequest.fromTransportModel(): SearchStocksResponseContext {
    return SearchStocksResponseContext(
        command = Command.SEARCH,
        request = filter.fromTransportModel(),
        debug = DebugMapper.fromTransportModel(debug)
    )
}

fun SearchStocksResponseContext.toTransportModel() : SearchStocksResponse {
    return SearchStocksResponse(
        responseType = command.value,
        result = state.toTransportModel(),
        body = response.map { it.toTransportModel() },
        errors = errors.map { it.toTransportModel() },
    )
}

fun SearchStocksFilter.fromTransportModel(): StockFilter {
    return StockFilter(
        searchString = searchString
    )
}

