package com.otus.otuskotlin.stocktrack.context

import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.RequestId
import com.otus.otuskotlin.stocktrack.model.State
import java.time.Instant

sealed interface Context<IN, OUT, SELF : Context<IN, OUT, SELF>> {
    val command: Command
    val state: State
    val errors: List<ErrorDescription>
    val debug: Debug
    val requestId: RequestId
    val startedAt: Instant
    val request: IN
    val response: OUT

    fun start(): SELF

    fun fail(error: ErrorDescription): SELF

    fun fail(error: Collection<ErrorDescription>): SELF

    fun finish(): SELF
}