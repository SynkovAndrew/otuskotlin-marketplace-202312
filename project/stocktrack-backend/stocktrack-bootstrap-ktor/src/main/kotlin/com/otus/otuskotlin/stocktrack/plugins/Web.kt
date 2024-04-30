package com.otus.otuskotlin.stocktrack.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureWeb() {
    install(CORS) {
        anyHost()
    }
}