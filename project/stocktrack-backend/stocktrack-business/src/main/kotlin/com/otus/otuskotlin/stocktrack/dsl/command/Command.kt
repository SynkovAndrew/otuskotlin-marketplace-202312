package com.otus.otuskotlin.stocktrack.dsl.command

import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State

fun <T : Context<*, *>> ChainDsl<T>.command(
    command: Command,
    process: T.() -> T
) {
    processor {
        this.name = command.name

        invokeOn {
            it.state == State.RUNNING &&
                    it.debug.mode == Debug.Mode.PROD &&
                    it.command == command
        }

        process {
            it.process().finish() as T
        }

        handleException { throwable, context ->
            context.fail(
                ErrorDescription(
                    message = throwable.message ?: "",
                    throwable = throwable
                )
            ) as T
        }
    }
}