package com.otus.otuskotlin.stocktrack.context

import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.RequestId
import com.otus.otuskotlin.stocktrack.model.State
import java.time.Instant

interface Context<REQUEST, RESPONSE> {
    val command: Command
    val state: State
    val errors: List<ErrorDescription>
    val debug: Debug
    val requestId: RequestId
    val startedAt: Instant
    val request: REQUEST
    val response: RESPONSE
}