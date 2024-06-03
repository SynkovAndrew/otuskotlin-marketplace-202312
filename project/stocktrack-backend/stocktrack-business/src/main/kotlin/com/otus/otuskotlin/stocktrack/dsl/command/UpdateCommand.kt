package com.otus.otuskotlin.stocktrack.dsl.command

import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest

fun ChainDsl<SingleStockResponseContext>.updateCommand(
    coreSettings: CoreSettings
) {
    processor {
        this.name = Command.UPDATE.name

        invokeOn {
            it.state == State.RUNNING &&
                    it.debug.mode == Debug.Mode.PROD &&
                    it.command == Command.UPDATE
        }

        process {
            StockRepositoryRequest(stock = it.request)
                .let { request -> coreSettings.prodStockRepository.update(request) }
                .let { response -> it.handleResponse(response) }
        }

        handleException { throwable, context ->
            context.fail(
                ErrorDescription(
                    message = throwable.message ?: "",
                    throwable = throwable
                )
            )
        }
    }
}