package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.SearchStocksResponseContext
import com.otus.otuskotlin.stocktrack.cor.chainBuilder
import com.otus.otuskotlin.stocktrack.dsl.command.commandPipeline
import com.otus.otuskotlin.stocktrack.dsl.command.searchCommand
import com.otus.otuskotlin.stocktrack.dsl.command.startProcessing
import com.otus.otuskotlin.stocktrack.dsl.stub.stubForDbErrorOnCommand
import com.otus.otuskotlin.stocktrack.dsl.stub.stubForRequestedStubNotFound
import com.otus.otuskotlin.stocktrack.dsl.stub.stubForSucceededSearchCommand
import com.otus.otuskotlin.stocktrack.dsl.stub.stubs
import com.otus.otuskotlin.stocktrack.model.Command

class SearchStocksResponseProcessor(
    val coreSettings: CoreSettings
) : ResponseProcessor<SearchStocksResponseContext> {

    override suspend fun execute(context: SearchStocksResponseContext): SearchStocksResponseContext {
        return chainBuilder<SearchStocksResponseContext> {
            startProcessing()

            commandPipeline(Command.SEARCH) {
                stubs {
                    stubForSucceededSearchCommand(coreSettings)
                    stubForDbErrorOnCommand()
                    stubForRequestedStubNotFound()
                }

                searchCommand(coreSettings)
            }

        }.execute(context)
    }
}
