package com.otus.otuskotlin.stocktrack.plugins.routing

import com.otus.otuskotlin.stocktrack.ApplicationSettings
import com.otus.otuskotlin.stocktrack.CommandBus
import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockSnapshotsRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.PredictStockSnapshotsRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.Request
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.UploadStockSnapshotsRequest
import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.context.GetStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.context.PostStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.snapshot.fromTransportModel
import com.otus.otuskotlin.stocktrack.stock.fromTransportModel
import com.otus.otuskotlin.stocktrack.stock.toTransportModel
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureStockRoutes(applicationSettings: ApplicationSettings) {
    val commandBus = CommandBus(applicationSettings)

    routing {
        route("/api/v1/stock") {
            post("find") {
                call.handleCall<FindStockRequest, SingleStockResponseContext>(applicationSettings)
            }

            post("/create") {
                call.handleCall<CreateStockRequest, SingleStockResponseContext>(applicationSettings)
            }

            post("/delete") {
                call.handleCall<DeleteStockRequest, SingleStockResponseContext>(applicationSettings)
            }

            post("/update") {
                call.handleCall<UpdateStockRequest, SingleStockResponseContext>(applicationSettings)
            }
            post("/search") {
                call.handleCall<SearchStocksRequest, SearchStocksResponseContext>(applicationSettings)
            }
        }
        route("/api/v1/snapshot") {
            post("/find") {
                call.handleCall<FindStockSnapshotsRequest, GetStockSnapshotsContext>(applicationSettings)
            }
            post("/predict") {
                call.handleCall<PredictStockSnapshotsRequest, GetStockSnapshotsContext>(applicationSettings)
            }
            post("/upload") {
                call.handleCall<UploadStockSnapshotsRequest, PostStockSnapshotsContext>(applicationSettings)
            }
        }
    }
}

suspend inline fun <reified R : Request, T : Context<*, *, T>> ApplicationCall.handleCall(
    applicationSettings: ApplicationSettings,
    commandBus: CommandBus = CommandBus(applicationSettings)
) {
    receive<R>()
        .fromTransportModel()
        .let { it as T }
        .let { context -> commandBus.processContext(context) }
        .let { this.respond(it.toTransportModel()) }
}
