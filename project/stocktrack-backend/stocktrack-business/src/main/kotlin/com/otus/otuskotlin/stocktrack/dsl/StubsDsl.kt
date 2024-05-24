package com.otus.otuskotlin.stocktrack.dsl

import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.LogLevel
import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.model.StockPermission
import java.util.*


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

fun ChainDsl<SingleStockResponseContext>.stubForSucceededUpdateCommand(
    coreSettings: CoreSettings
) {
    processor {
        val logger = coreSettings.loggerProvider.logger("stubForSucceededUpdateCommand")
        this.name = "stubForSucceededUpdateCommand"

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.UPDATE &&
                    it.debug.mode == Debug.Mode.STUB &&
                    it.debug.stub == Debug.Stub.SUCCESS
        }

        process {
            logger.callLogged(it.requestId.asString(), LogLevel.DEBUG) {
                it.copy(
                    state = State.FINISHED,
                    response = it.request.copy(
                        name = it.request.name,
                        category = it.request.category
                    )
                )
            }
        }
    }
}

fun ChainDsl<SingleStockResponseContext>.stubForSucceededDeleteCommand(
    coreSettings: CoreSettings
) {
    processor {
        val logger = coreSettings.loggerProvider.logger("stubForSucceededDeleteCommand")
        this.name = "stubForSucceededDeleteCommand"

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.DELETE &&
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

fun ChainDsl<SingleStockResponseContext>.stubForSucceededFindCommand(
    coreSettings: CoreSettings
) {
    processor {
        val logger = coreSettings.loggerProvider.logger("stubForSucceededFindCommand")
        this.name = "stubForSucceededFindCommand"

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.FIND &&
                    it.debug.mode == Debug.Mode.STUB &&
                    it.debug.stub == Debug.Stub.SUCCESS
        }

        process {
            logger.callLogged(it.requestId.asString(), LogLevel.DEBUG) {
                it.copy(
                    state = State.FINISHED,
                    response = Stock(
                        id = it.request.id,
                        name = UUID.randomUUID().toString(),
                        category = Stock.Category.entries.random()
                    )
                )
            }
        }
    }
}

fun ChainDsl<SearchStocksResponseContext>.stubForSucceededSearchCommand(
    coreSettings: CoreSettings
) {
    processor {
        val logger = coreSettings.loggerProvider.logger("stubForSucceededSearchCommand")
        this.name = "stubForSucceededSearchCommand"

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.SEARCH &&
                    it.debug.mode == Debug.Mode.STUB &&
                    it.debug.stub == Debug.Stub.SUCCESS
        }

        process {
            logger.callLogged(it.requestId.asString(), LogLevel.DEBUG) {
                it.copy(
                    state = State.FINISHED,
                    response = listOf(
                        Stock(
                            id = Stock.Id(value = "1"),
                            name = "Gazprom",
                            category = Stock.Category.SHARE,
                            permissions = setOf(StockPermission.READ)
                        )
                    )
                )
            }
        }
    }
}

fun ChainDsl<SingleStockResponseContext>.stubForDbErrorOnCommand() {
    processor {
        this.name = "stubForDbErrorOnCommand"

        invokeOn {
            it.state == State.RUNNING &&
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

fun ChainDsl<SearchStocksResponseContext>.stubForDbErrorOnCommand() {
    processor {
        this.name = "stubForDbErrorOnCommand"

        invokeOn {
            it.state == State.RUNNING &&
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
            it.state == State.RUNNING && it.debug.mode == Debug.Mode.STUB
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

fun ChainDsl<SearchStocksResponseContext>.stubForRequestedStubNotFound() {
    processor {
        this.name = "stubForRequestedCaseNotFound"

        invokeOn {
            it.state == State.RUNNING && it.debug.mode == Debug.Mode.STUB
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