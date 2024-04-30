package com.otus.otuskotlin.stocktrack.plugins

import com.auth0.jwk.UrlJwkProvider
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.net.URL

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("jwtAuth") {
            realm = "marketplace"
            verifier(
                UrlJwkProvider(
                    URL("http://localhost:8484/realms/marketplace/protocol/openid-connect/certs")
                ),
                "http://localhost:8484/realms/marketplace",
            )
            validate { credential ->
                credential.payload.takeIf { it.issuer != null }?.let { JWTPrincipal(it) }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Authorization token not provided or invalid")
            }
        }
    }
}