package com.otus.otuskotlin.stocktrack.dsl.stub

import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State

fun <T : Context<*, *, T>> ChainDsl<T>.stubForFailedCauseBadRequest() {
    processor {
        this.name = "stubForFailedCauseWrongId"

        invokeOn {
            it.state == State.RUNNING
                    && it.debug.mode == Debug.Mode.STUB
                    && it.debug.stub == Debug.Stub.BAD_REQUEST
        }

        process {
            it.fail(
                ErrorDescription(
                    code = "stub-bad-request-error",
                    group = "stub-bad-request-error-group",
                    field = "no",
                    message = "Bad request"
                )
            )
        }
    }
}