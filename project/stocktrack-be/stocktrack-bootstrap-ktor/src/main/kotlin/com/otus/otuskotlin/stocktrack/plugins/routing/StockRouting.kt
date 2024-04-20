package com.otus.otuskotlin.stocktrack.plugins.routing

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.DeleteStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.FindStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.SearchStocksRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.StockId
import com.otus.otuskotlin.stocktrack.api.v1.models.UpdateStockRequest
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.model.StockPermission
import com.otus.otuskotlin.stocktrack.stock.fromTransportModel
import com.otus.otuskotlin.stocktrack.stock.toTransportModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureStockRoutes() {
    routing {
        route("/api/v1/stock") {
            post("find") {
                call.receive<FindStockRequest>()
                    .fromTransportModel()
                    .let { context ->
                        StubStockProvider.provide()
                            .firstOrNull { context.request.id == it.id }
                            ?.let {
                                context.copy(
                                    state = State.RUNNING,
                                    response = it
                                )
                            }
                            ?.let { call.respond(it.toTransportModel()) }
                            ?: call.respond(HttpStatusCode.NotFound)
                    }
            }

            post("/create") {
                call.respond(
                    call.receive<CreateStockRequest>()
                        .fromTransportModel()
                        .toTransportModel()
                )
            }

            post("/delete") {
                call.respond(
                    call.receive<DeleteStockRequest>()
                        .fromTransportModel()
                        .toTransportModel()
                )
            }

            post("/update") {
                call.respond(
                    call.receive<UpdateStockRequest>()
                        .fromTransportModel()
                        .toTransportModel()
                )
            }

            post("/search") {
                call.respond(
                    call.receive<SearchStocksRequest>()
                        .fromTransportModel()
                        .copy(response = StubStockProvider.provide())
                        .toTransportModel()
                )
            }
        }
    }
}

object StubStockProvider {
    fun provide(): List<Stock> {
        return listOf(
            Stock(
                id = Stock.Id(value = "1"),
                name = "Gazprom",
                category = Stock.Category.SHARE,
                permissions = setOf(StockPermission.READ)
            ),
            Stock(
                id = Stock.Id(value = "2"),
                name = "Rosbank",
                category = Stock.Category.BOND,
                permissions = setOf(StockPermission.READ)
            ),
            Stock(
                id = Stock.Id(value = "3"),
                name = "Vk",
                category = Stock.Category.SHARE,
                permissions = setOf(StockPermission.READ)
            )
        )
    }
}