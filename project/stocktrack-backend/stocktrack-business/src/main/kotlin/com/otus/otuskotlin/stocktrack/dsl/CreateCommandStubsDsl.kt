package com.otus.otuskotlin.stocktrack.dsl

import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.LogLevel
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State

fun ChainDsl<SingleStockResponseContext>.stubForSucceededCreateCommand(
    coreSettings: CoreSettings
) {
    processor {
        val logger = coreSettings.loggerProvider.logger("stubForSucceededCreateCommand")
        this.name = "stubForSucceededCreateCommand"

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.CREATE &&
                    it.debug.mode == Debug.Mode.STUB &&
                    it.debug.stub == Debug.Stub.SUCCESS
        }

        process {
            logger.callLogged(it.requestId.asString(), LogLevel.DEBUG) {
                it.copy(state = State.FINISHED, response = it.request)
            }
        }
    }
}

fun ChainDsl<SingleStockResponseContext>.stubForDbErrorOnCreateCommand() {
    processor {
        this.name = "stubForDbErrorOnCreateCommand"

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.CREATE &&
                    it.debug.mode == Debug.Mode.STUB &&
                    it.debug.stub == Debug.Stub.DATABASE_ERROR
        }

        process {
            it.copy(
                state = State.FAILED,
                errors = it.errors + ErrorDescription(
                    code = "db-error",
                    group = "db-error-group",
                    field = "no",
                    message = "Failed to access database"
                )
            )
        }
    }
}

fun ChainDsl<SingleStockResponseContext>.stubForRequestedStubNotFound() {
    processor {
        this.name = "stubForRequestedCaseNotFound"

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.CREATE &&
                    it.debug.mode == Debug.Mode.STUB
        }

        process {
            it.copy(
                state = State.FAILED,
                errors = it.errors + ErrorDescription(
                    code = "stub-not-found-error",
                    group = "stub-not-found-error-group",
                    field = "no",
                    message = "Failed to find stub"
                )
            )
        }
    }
}