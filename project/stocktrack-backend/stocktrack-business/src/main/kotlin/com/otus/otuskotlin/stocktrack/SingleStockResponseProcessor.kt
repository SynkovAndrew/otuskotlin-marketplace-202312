package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.chainBuilder
import com.otus.otuskotlin.stocktrack.dsl.command
import com.otus.otuskotlin.stocktrack.dsl.commandPipeline
import com.otus.otuskotlin.stocktrack.dsl.startProcessing
import com.otus.otuskotlin.stocktrack.dsl.stubForDbErrorOnCreateCommand
import com.otus.otuskotlin.stocktrack.dsl.stubForRequestedStubNotFound
import com.otus.otuskotlin.stocktrack.dsl.stubForSucceededCreateCommand
import com.otus.otuskotlin.stocktrack.dsl.stubs
import com.otus.otuskotlin.stocktrack.dsl.validateCreateCommandCategoryProperty
import com.otus.otuskotlin.stocktrack.dsl.validateCreateCommandIdProperty
import com.otus.otuskotlin.stocktrack.dsl.validateCreateCommandNameProperty
import com.otus.otuskotlin.stocktrack.dsl.validation
import com.otus.otuskotlin.stocktrack.model.Command

class SingleStockResponseProcessor(val coreSettings: CoreSettings) {

    suspend fun execute(context: SingleStockResponseContext): SingleStockResponseContext {
        return chainBuilder<SingleStockResponseContext> {
            startProcessing()

            commandPipeline(Command.CREATE) {
                stubs {
                    stubForSucceededCreateCommand(coreSettings)
                    stubForDbErrorOnCreateCommand()
                    stubForRequestedStubNotFound()
                }
                validation {
                    validateCreateCommandNameProperty()
                    validateCreateCommandCategoryProperty()
                }

                command(Command.CREATE) {
                    copy(response = request)
                }
            }

            commandPipeline(Command.UPDATE) {
                stubs {
                }
                validation {
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
            }

            commandPipeline(Command.FIND) {
                stubs {
                }
                validation {
                }

                command(Command.FIND) {
                    copy(response = StubStockRepository.findById(context.request.id))
                }
            }

            commandPipeline(Command.DELETE) {
                stubs {
                }
                validation {
                }

                command(Command.DELETE) {
                    copy(response = StubStockRepository.findById(context.request.id))
                }
            }
        }.execute(context)
    }
}

