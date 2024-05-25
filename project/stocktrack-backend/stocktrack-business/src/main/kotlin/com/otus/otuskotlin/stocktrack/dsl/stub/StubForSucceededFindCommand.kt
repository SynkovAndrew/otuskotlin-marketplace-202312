package com.otus.otuskotlin.stocktrack.dsl.stub

import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.LogLevel
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.model.Stock
import java.util.*

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