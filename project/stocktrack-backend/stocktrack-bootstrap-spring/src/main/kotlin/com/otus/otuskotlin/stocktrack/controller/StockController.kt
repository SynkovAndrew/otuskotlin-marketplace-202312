package com.otus.otuskotlin.stocktrack.controller

import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/stock")
class StockController {

    @PostMapping("/find")
    fun find(request: FindStockRequest): FindStockResponse {
        
    }
}