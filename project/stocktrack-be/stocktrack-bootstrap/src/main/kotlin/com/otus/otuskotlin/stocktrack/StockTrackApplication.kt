package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.plugins.configureAuthentication
import com.otus.otuskotlin.stocktrack.plugins.configureRouting
import com.otus.otuskotlin.stocktrack.plugins.configureSerialization
import com.otus.otuskotlin.stocktrack.plugins.configureWeb
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8090, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureAuthentication()
    configureSerialization()
    configureRouting()
    configureWeb()
}
