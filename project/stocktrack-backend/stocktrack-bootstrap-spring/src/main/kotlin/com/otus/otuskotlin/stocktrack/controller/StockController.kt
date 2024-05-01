package com.otus.otuskotlin.stocktrack.controller

import com.otus.otuskotlin.stocktrack.ApplicationSettings
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.Response
import com.otus.otuskotlin.stocktrack.processSingleStockResponseContext
import com.otus.otuskotlin.stocktrack.stock.fromTransportModel
import com.otus.otuskotlin.stocktrack.stock.toTransportModel
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/stock")
class StockController(
    private val applicationSettings: ApplicationSettings
) {

    @PostMapping("/find")
    suspend fun find(request: FindStockRequest): Response {
        return request
            .fromTransportModel()
            .let { applicationSettings.processSingleStockResponseContext(it, this::class) }
            .toTransportModel()
    }

    @PostMapping("/create")
    suspend fun create(request: CreateStockRequest): Response {
        return request
            .fromTransportModel()
            .let { applicationSettings.processSingleStockResponseContext(it, this::class) }
            .toTransportModel()
    }
}