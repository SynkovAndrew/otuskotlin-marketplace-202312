package com.otus.otuskotlin.stocktrack.plugins

import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.otus.otuskotlin.stocktrack.LoggerWrapper
import com.otus.otuskotlin.stocktrack.logbackLoggerWrapper
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.URI
import java.net.URL

fun Application.configureAuthentication() {
    val logger: LoggerWrapper = logbackLoggerWrapper(this::class)

    install(Authentication) {
        jwt("auth-jwt") {
            realm = "stocktrack"
            verifier(
                UrlJwkProvider(
                    URI("http://localhost:8484/realms/stocktrack/protocol/openid-connect/certs").toURL()
                ),
                "http://localhost:8484/realms/stocktrack",
            )
            validate { credential ->
                logger.info("Validating JWT credential")
                credential.payload.takeIf { it.issuer != null }?.let { JWTPrincipal(it) }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Authorization token not provided or invalid")
            }
        }

    }
    routing {
        authenticate("auth-jwt") {
            get("/secure") {
                call.respondText("This is a secure endpoint")
            }
        }

        get("/") {
            call.respondText("Hello World")
        }
    }
}