package com.otus.otuskotlin.stocktrack.dsl.command

import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.stock.StockIdRepositoryRequest

fun ChainDsl<SingleStockResponseContext>.deleteCommand(
    coreSettings: CoreSettings
) {
    processor {
        this.name = Command.DELETE.name

        invokeOn {
            it.state == State.RUNNING &&
                    it.debug.mode == Debug.Mode.PROD &&
                    it.command == Command.DELETE
        }

        process {
            StockIdRepositoryRequest(stockId = it.request.id, lock = it.request.lock)
                .let { request -> coreSettings.prodStockRepository.delete(request) }
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