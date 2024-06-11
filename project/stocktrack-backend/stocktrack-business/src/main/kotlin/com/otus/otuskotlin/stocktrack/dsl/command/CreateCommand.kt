package com.otus.otuskotlin.stocktrack.dsl.command

import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.stock.ErrorStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkWithErrorsStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryResponse

fun ChainDsl<SingleStockResponseContext>.createCommand(
    coreSettings: CoreSettings
) {
    processor {
        this.name = Command.CREATE.name

        invokeOn {
            it.state == State.RUNNING &&
                    it.debug.mode == Debug.Mode.PROD &&
                    it.command == Command.CREATE
        }

        process {
            StockRepositoryRequest(stock = it.request)
                .let { request -> coreSettings.prodStockRepository.create(request) }
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