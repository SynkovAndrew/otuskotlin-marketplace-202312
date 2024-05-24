package com.otus.otuskotlin.stocktrack.dsl

import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State
import java.time.Instant


fun ChainDsl<SearchStocksResponseContext>.command(
    command: Command,
    process: SearchStocksResponseContext.() -> SearchStocksResponseContext
) {
    processor {
        this.name = command.name

        invokeOn {
            it.state == State.RUNNING &&
                    it.debug.mode == Debug.Mode.PROD &&
                    it.command == command
        }

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
        name = "startProcessing"
        invokeOn { it.state == State.NONE }
        process {
            it.copy(
                startedAt = Instant.now(),
                state = State.RUNNING
            )
        }
    }
}

fun ChainDsl<SearchStocksResponseContext>.stubs(
    block: ChainDsl<SearchStocksResponseContext>.() -> Unit
) {
    chain {
        block()
        invokeOn { it.state == State.RUNNING && it.debug.mode == Debug.Mode.STUB }
    }
}

fun ChainDsl<SearchStocksResponseContext>.commandPipeline(
    command: Command,
    block: ChainDsl<SearchStocksResponseContext>.() -> Unit
) {
    chain {
        block()
        invokeOn { it.state == State.RUNNING && it.command == command }
    }
}

fun ChainDsl<SearchStocksResponseContext>.validation(
    block: ChainDsl<SearchStocksResponseContext>.() -> Unit
) {
    chain {
        block()
        invokeOn { it.state == State.RUNNING }
    }
}
