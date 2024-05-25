package com.otus.otuskotlin.stocktrack.dsl.stub

import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.LogLevel
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.State

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