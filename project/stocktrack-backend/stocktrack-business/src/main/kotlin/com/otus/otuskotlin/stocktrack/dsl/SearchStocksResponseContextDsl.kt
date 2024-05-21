package com.otus.otuskotlin.stocktrack.dsl

import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State

fun ChainDsl<SearchStocksResponseContext>.command(
    command: Command,
    process: SearchStocksResponseContext.() -> SearchStocksResponseContext
) {
    processor {
        this.name = command.name

        invokeOn { it.state == State.RUNNING && it.command == command }

        process {
            it.process().copy(state = State.FINISHED)
        }

        handleException { throwable, context ->
           context.copy(
                state = State.FAILED,
                errors = context.errors + ErrorDescription(
                    message = throwable.message ?: "",
                    throwable = throwable
                )
            )
        }
    }
}

fun ChainDsl<SearchStocksResponseContext>.startProcessing() {
    processor {
        name = "START"
        invokeOn { it.state == State.NONE }
        process {
            it.copy(state = State.RUNNING)
        }
    }
}