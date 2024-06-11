package com.otus.otuskotlin.stocktrack.dsl.stub

import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.LogLevel
import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.stock.ErrorStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkWithErrorsStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest

fun ChainDsl<SingleStockResponseContext>.stubForSucceededCreateCommand(
    coreSettings: CoreSettings
) {
    processor {
        this.name = "stubForSucceededCreateCommand"

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.CREATE &&
                    it.debug.mode == Debug.Mode.STUB &&
                    it.debug.stub == Debug.Stub.SUCCESS
        }

        process {
            StockRepositoryRequest(stock = it.request)
                .let { request -> coreSettings.stubStockRepository.create(request) }
                .let { response -> it.copy(response = (response as OkStockRepositoryResponse).data).finish() }
        }
    }
}