package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.State

class SearchStocksResponseProcessor(val coreSettings: CoreSettings) {

    suspend fun execute(context: SearchStocksResponseContext): SearchStocksResponseContext {

        return context.copy(
            state = State.RUNNING,
            response = when (context.command) {
                Command.SEARCH -> StubStockRepository.findAll()
                    .filter { stock ->
                        context.request.searchString
                            ?.let { stock.name.contains(it, true) }
                            ?: true
                    }

                else -> throw IllegalArgumentException("Command ${context.command} not supported")
            }
        )
    }
}
