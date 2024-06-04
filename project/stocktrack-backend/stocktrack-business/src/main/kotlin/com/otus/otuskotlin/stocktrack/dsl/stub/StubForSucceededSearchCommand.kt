package com.otus.otuskotlin.stocktrack.dsl.stub

import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.LogLevel
import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.stock.StockPermission

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