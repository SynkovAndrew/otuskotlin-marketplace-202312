package com.otus.otuskotlin.stocktrack.dsl.stub

import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State

fun <T : Context<*, *>> ChainDsl<T>.stubForDbErrorOnCommand() {
    processor {
        this.name = "stubForDbErrorOnCommand"

        invokeOn {
            it.state == State.RUNNING &&
                    it.debug.mode == Debug.Mode.STUB &&
                    it.debug.stub == Debug.Stub.DATABASE_ERROR
        }

        process {
            it.fail(
                ErrorDescription(
                    code = "db-error",
                    group = "db-error-group",
                    field = "no",
                    message = "Failed to access database"
                )
            ) as T
        }
    }
}