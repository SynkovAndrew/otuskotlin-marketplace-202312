package com.otus.otuskotlin.stocktrack.dsl

import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State
import java.time.Instant

fun ChainDsl<SingleStockResponseContext>.command(
    command: Command,
    process: SingleStockResponseContext.() -> SingleStockResponseContext
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

fun ChainDsl<SingleStockResponseContext>.startProcessing() {
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

fun ChainDsl<SingleStockResponseContext>.stubs(
    block: ChainDsl<SingleStockResponseContext>.() -> Unit
) {
    chain {
        block()
        invokeOn { it.state == State.RUNNING && it.debug.mode == Debug.Mode.STUB }
    }
}

fun ChainDsl<SingleStockResponseContext>.commandPipeline(
    command: Command,
    block: ChainDsl<SingleStockResponseContext>.() -> Unit
) {
    chain {
        block()
        invokeOn { it.state == State.RUNNING && it.command == command }
    }
}

fun ChainDsl<SingleStockResponseContext>.validation(
    block: ChainDsl<SingleStockResponseContext>.() -> Unit
) {
    chain {
        block()
        invokeOn { it.state == State.RUNNING }
    }
}
