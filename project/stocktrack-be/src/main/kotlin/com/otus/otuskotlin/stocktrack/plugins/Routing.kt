package com.otus.otuskotlin.stocktrack.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable
import java.util.UUID

fun Application.configureRouting() {
    routing {
        authenticate("jwtAuth") {
            get("/jwt") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("preferred_username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())

                call.respondText("$username! Token expires in $expiresAt ms. ${principal.payload.subject}")
            }

/*            route("/api/v1") {
                get("/products") {
                    call.respond(stocksStub())
                }
            }*/
        }

        route("/api/v1") {
            get("/stocks") {
                call.respond(stocksStub())
            }
        }

        get("/hi") {
            call.respondText("Hey! Hello World!")
        }
    }
}

private fun stocksStub(): List<StockJsonBean> {
    return listOf(
        StockJsonBean(
            id = UUID.randomUUID().toString(),
            name = "Gazprom",
            value = 11.3
        ),
        StockJsonBean(
            id = UUID.randomUUID().toString(),
            name = "Alpha Bank",
            value = 5.3
        ),
        StockJsonBean(
            id = UUID.randomUUID().toString(),
            name = "Yandex",
            value = 21.3
        )
    )
}

@Serializable
data class StockJsonBean(
    val id: String,
    val name: String,
    val value: Double
)