package com.otus.otuskotlin.stocktrack.plugins.routing

import com.otus.otuskotlin.stocktrack.ApplicationSettings
import com.otus.otuskotlin.stocktrack.StubStockRepository
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.Request
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockRequest
import com.otus.otuskotlin.stocktrack.stock.fromTransportModel
import com.otus.otuskotlin.stocktrack.stock.toTransportModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureStockRoutes(applicationSettings: ApplicationSettings) {
    routing {
        route("/api/v1/stock") {
            post("find") { call.processRequestWithSingleStockResponse<FindStockRequest>(applicationSettings) }

            post("/create") { call.processRequestWithSingleStockResponse<CreateStockRequest>(applicationSettings) }

            post("/delete") { call.processRequestWithSingleStockResponse<DeleteStockRequest>(applicationSettings) }

            post("/update") { call.processRequestWithSingleStockResponse<UpdateStockRequest>(applicationSettings) }
            post("/search") {
                call.receive<SearchStocksRequest>()
                    .fromTransportModel()
                    .let { context ->
                        withExceptionHandling(call) {
                            applicationSettings.searchStocksResponseProcessor
                                .execute(context)
                        }
                    }
                    ?.let { call.respond(it.toTransportModel()) }
            }
        }
    }
}

suspend inline fun <reified T : Request> ApplicationCall.processRequestWithSingleStockResponse(
    applicationSettings: ApplicationSettings
){
    receive<T>()
        .fromTransportModel()
        .let { context ->
            withExceptionHandling(this) {
                applicationSettings.singleStockResponseProcessor
                    .execute(context)
            }
        }
        ?.let { this.respond(it.toTransportModel()) }
}

suspend fun <T> withExceptionHandling(call: ApplicationCall, block: suspend () -> T): T? {
    try {
        return block()
    } catch (ex: StubStockRepository.StockNotFoundException) {
        call.respond(HttpStatusCode.NotFound)
        return null
    }
}