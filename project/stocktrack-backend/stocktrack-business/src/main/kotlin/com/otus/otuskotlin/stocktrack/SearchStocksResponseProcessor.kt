package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.cor.chainBuilder
import com.otus.otuskotlin.stocktrack.dsl.command
import com.otus.otuskotlin.stocktrack.dsl.startProcessing
import com.otus.otuskotlin.stocktrack.model.Command

class SearchStocksResponseProcessor(val coreSettings: CoreSettings) {

    suspend fun execute(context: SearchStocksResponseContext): SearchStocksResponseContext {
        return chainBuilder<SearchStocksResponseContext> {
            startProcessing()

            command(Command.SEARCH) {
                copy(
                    response = StubStockRepository.findAll()
                        .filter { stock ->
                            context.request.searchString
                                ?.let { stock.name.contains(it, true) }
                                ?: true
                        }
                )
            }

        }.execute(context)
    }
}
