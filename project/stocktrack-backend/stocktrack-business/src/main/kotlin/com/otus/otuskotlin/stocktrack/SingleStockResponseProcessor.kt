package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.chainBuilder
import com.otus.otuskotlin.stocktrack.dsl.command.command
import com.otus.otuskotlin.stocktrack.dsl.command.commandPipeline
import com.otus.otuskotlin.stocktrack.dsl.command.startProcessing
import com.otus.otuskotlin.stocktrack.dsl.stub.stubForDbErrorOnCommand
import com.otus.otuskotlin.stocktrack.dsl.stub.stubForRequestedStubNotFound
import com.otus.otuskotlin.stocktrack.dsl.stub.stubForSucceededCreateCommand
import com.otus.otuskotlin.stocktrack.dsl.stub.stubForSucceededDeleteCommand
import com.otus.otuskotlin.stocktrack.dsl.stub.stubForSucceededFindCommand
import com.otus.otuskotlin.stocktrack.dsl.stub.stubForSucceededUpdateCommand
import com.otus.otuskotlin.stocktrack.dsl.stub.stubs
import com.otus.otuskotlin.stocktrack.dsl.validation.validateIdProperty
import com.otus.otuskotlin.stocktrack.dsl.validation.validateNameProperty
import com.otus.otuskotlin.stocktrack.dsl.validation.validateStockCategoryProperty
import com.otus.otuskotlin.stocktrack.dsl.validation.validation
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Stock

class SingleStockResponseProcessor(
    val coreSettings: CoreSettings
) : ResponseProcessor<Stock, Stock, SingleStockResponseContext> {

    override suspend fun execute(context: SingleStockResponseContext): SingleStockResponseContext {
        return chainBuilder<SingleStockResponseContext> {
            startProcessing()

            commandPipeline(Command.CREATE) {
                stubs {
                    stubForSucceededCreateCommand(coreSettings)
                    stubForDbErrorOnCommand()
                    stubForRequestedStubNotFound()
                }
                validation {
                    validateNameProperty()
                    validateStockCategoryProperty()
                }

                command(Command.CREATE) {
                    copy(response = request)
                }
            }

            commandPipeline(Command.UPDATE) {
                stubs {
                    stubForSucceededUpdateCommand(coreSettings)
                    stubForDbErrorOnCommand()
                    stubForRequestedStubNotFound()
                }
                validation {
                    validateIdProperty()
                    validateNameProperty()
                    validateStockCategoryProperty()
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
                    stubForSucceededFindCommand(coreSettings)
                    stubForDbErrorOnCommand()
                    stubForRequestedStubNotFound()
                }
                validation {
                    validateIdProperty()
                }

                command(Command.FIND) {
                    copy(response = StubStockRepository.findById(context.request.id))
                }
            }

            commandPipeline(Command.DELETE) {
                stubs {
                    stubForSucceededDeleteCommand(coreSettings)
                    stubForDbErrorOnCommand()
                    stubForRequestedStubNotFound()
                }
                validation {
                    validateIdProperty()
                }

                command(Command.DELETE) {
                    copy(response = StubStockRepository.findById(context.request.id))
                }
            }
        }.execute(context)
    }
}

