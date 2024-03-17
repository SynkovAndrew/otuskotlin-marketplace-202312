package com.otus.otuskotlin.marketplace

import com.otus.otuskotlin.marketplace.plugins.configureAuthentication
import com.otus.otuskotlin.marketplace.plugins.configureRouting
import com.otus.otuskotlin.marketplace.plugins.configureSerialization
import com.otus.otuskotlin.marketplace.plugins.configureWeb
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
