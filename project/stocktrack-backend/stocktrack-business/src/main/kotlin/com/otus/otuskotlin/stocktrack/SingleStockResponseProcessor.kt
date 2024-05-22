package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.chainBuilder
import com.otus.otuskotlin.stocktrack.dsl.command
import com.otus.otuskotlin.stocktrack.dsl.commands
import com.otus.otuskotlin.stocktrack.dsl.startProcessing
import com.otus.otuskotlin.stocktrack.dsl.stubForDbErrorOnCreateCommand
import com.otus.otuskotlin.stocktrack.dsl.stubForRequestedStubNotFound
import com.otus.otuskotlin.stocktrack.dsl.stubForSucceededCreateCommand
import com.otus.otuskotlin.stocktrack.dsl.stubs
import com.otus.otuskotlin.stocktrack.model.Command

class SingleStockResponseProcessor(val coreSettings: CoreSettings) {

    suspend fun execute(context: SingleStockResponseContext): SingleStockResponseContext {
        return chainBuilder<SingleStockResponseContext> {
            startProcessing()

            commands {
                command(Command.CREATE) {
                    copy(response = request)
                }

                command(Command.UPDATE) {
                    copy(
                        response = StubStockRepository.findById(context.request.id)
                            .copy(
                                name = context.request.name,
                                category = context.request.category
                            )
                    )
                }

                command(Command.FIND) {
                    copy(response = StubStockRepository.findById(context.request.id))
                }

                command(Command.DELETE) {
                    copy(response = StubStockRepository.findById(context.request.id))
                }
            }

            stubs {
                stubForSucceededCreateCommand(coreSettings)
                stubForDbErrorOnCreateCommand()
                stubForRequestedStubNotFound()
            }

        }.execute(context)
    }
}

