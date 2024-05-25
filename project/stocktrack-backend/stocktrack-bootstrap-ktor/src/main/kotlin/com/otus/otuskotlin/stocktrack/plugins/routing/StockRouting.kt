package com.otus.otuskotlin.stocktrack.plugins.routing

import com.otus.otuskotlin.stocktrack.ApplicationSettings
import com.otus.otuskotlin.stocktrack.CommandBus
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.Request
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockRequest
import com.otus.otuskotlin.stocktrack.stock.fromTransportModel
import com.otus.otuskotlin.stocktrack.stock.toTransportModel
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureStockRoutes(applicationSettings: ApplicationSettings) {
    routing {
        route("/api/v1/stock") {
            post("find") {
                call.handleCall<FindStockRequest>(applicationSettings)
            }

            post("/create") {
                call.handleCall<CreateStockRequest>(applicationSettings)
            }

            post("/delete") {
                call.handleCall<DeleteStockRequest>(applicationSettings)
            }

            post("/update") {
                call.handleCall<UpdateStockRequest>(applicationSettings)
            }
            post("/search") {
                call.handleCall<SearchStocksRequest>(applicationSettings)
            }
        }
    }
}

suspend inline fun <reified T : Request> ApplicationCall.handleCall(
    applicationSettings: ApplicationSettings,
    commandBus: CommandBus = CommandBus(applicationSettings)
) {
    receive<T>()
        .fromTransportModel()
        .let { context -> commandBus.processContext(context) }
        .let { this.respond(it.toTransportModel()) }
}
