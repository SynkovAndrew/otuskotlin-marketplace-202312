package com.otus.otuskotlin.stocktrack.context

import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.Error
import com.otus.otuskotlin.stocktrack.model.RequestId
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.stub.Stub
import java.time.Instant
import java.util.UUID

sealed class Context(
    val state: State = State.NONE,
    val errors: Set<Error> = emptySet(),
    val debugMode: Debug.Mode = Debug.Mode.NONE,
    val stub: Stub = Stub.NONE,
    val requestId: RequestId = RequestId(value = UUID.randomUUID().toString()),
    val startedAt: Instant = Instant.now(),
)