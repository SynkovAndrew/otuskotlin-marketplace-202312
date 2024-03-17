package com.otus.otuskotlin.marketplace.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureWeb() {
    install(CORS) {
        anyHost()
    }
}