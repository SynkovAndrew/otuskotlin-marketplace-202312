package com.otus.otuskotlin.stocktrack.dsl.stub

import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State

fun <T : Context<*, *, T>> ChainDsl<T>.stubForRequestedStubNotFound() {
    processor {
        this.name = "stubForRequestedCaseNotFound"

        invokeOn {
            it.state == State.RUNNING && it.debug.mode == Debug.Mode.STUB
        }

        process {
            it.fail(
                ErrorDescription(
                    code = "stub-not-found-error",
                    group = "stub-not-found-error-group",
                    field = "no",
                    message = "Failed to find stub"
                )
            )
        }
    }
}