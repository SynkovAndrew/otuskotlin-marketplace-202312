package com.otus.otuskotlin.stocktrack.dsl.command

import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.stock.StockFilterRepositoryRequest

fun ChainDsl<SearchStocksResponseContext>.searchCommand(
    coreSettings: CoreSettings
) {
    processor {
        this.name = Command.SEARCH.name

        invokeOn {
            it.state == State.RUNNING &&
                    it.debug.mode == Debug.Mode.PROD &&
                    it.command == Command.SEARCH
        }

        process {
            StockFilterRepositoryRequest(name = it.request.searchString, category = it.request.category)
                .let { request -> coreSettings.prodStockRepository.search(request) }
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