package com.otus.otuskotlin.stocktrack.plugins

import com.otus.otuskotlin.stocktrack.LoggerWrapper
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.logbackLoggerWrapper
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.model.StockPermission
import com.otus.otuskotlin.stocktrack.toLog
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID


fun Application.configureRouting() {
    val logger: LoggerWrapper = logbackLoggerWrapper(this::class)
    routing {
        authenticate("jwtAuth") {
            get("/jwt") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("preferred_username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())

                call.respondText("$username! Token expires in $expiresAt ms. ${principal.payload.subject}")
            }
        }

        get("/hi") {
            val singleStockResponseContext = SingleStockResponseContext(
                command = Command.CREATE,
                request = Stock(
                    name = "Test",
                    category = Stock.Category.BOND,
                    permissions = setOf(StockPermission.READ)
                ),
                state = State.RUNNING,
                startedAt = Instant.now()
            )
            call.respondText("Hey! Hello World!")
        //    call.application.log.info("Hey! Hello World!")
            logger.info(
                "Hello world",
                singleStockResponseContext.toLog("12345"),
                mapOf("retro" to 10)
            )
        }
    }
}
