package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksResponse
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockResponse
import com.otus.otuskotlin.stocktrack.model.Command
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*

class StockClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson {
            }
        }
    }

    suspend fun createStock(request: CreateStockRequest): CreateStockResponse {
        return post<CreateStockRequest, CreateStockResponse>(Command.CREATE.value, request)
    }

    suspend fun updateStock(request: UpdateStockRequest): UpdateStockResponse {
        return post<UpdateStockRequest, UpdateStockResponse>(Command.UPDATE.value, request)
    }

    suspend fun deleteStock(request: DeleteStockRequest): DeleteStockResponse {
        return post<DeleteStockRequest, DeleteStockResponse>(Command.DELETE.value, request)
    }

    suspend fun findStock(request: FindStockRequest): FindStockResponse {
        return post<FindStockRequest, FindStockResponse>(Command.FIND.value, request)
    }

    suspend fun searchStocks(request: SearchStocksRequest): SearchStocksResponse {
        return post<SearchStocksRequest, SearchStocksResponse>(Command.SEARCH.value, request)
    }

    private suspend inline fun <reified Req, reified Res> post(command: String, request: Req): Res {
        return client.use {
            val httpResponse = it.post("$SERVER_HOST/$STOCK_API/$command") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }
            httpResponse.body<Res>()
        }
    }

    private companion object {
        const val SERVER_HOST = "http://localhost:8080"
        const val STOCK_API = "api/v1/stock"
    }
}