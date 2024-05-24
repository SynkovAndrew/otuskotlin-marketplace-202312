package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.chainBuilder
import com.otus.otuskotlin.stocktrack.dsl.command
import com.otus.otuskotlin.stocktrack.dsl.commandPipeline
import com.otus.otuskotlin.stocktrack.dsl.startProcessing
import com.otus.otuskotlin.stocktrack.dsl.stubForDbErrorOnCommand
import com.otus.otuskotlin.stocktrack.dsl.stubForRequestedStubNotFound
import com.otus.otuskotlin.stocktrack.dsl.stubForSucceededCreateCommand
import com.otus.otuskotlin.stocktrack.dsl.stubForSucceededDeleteCommand
import com.otus.otuskotlin.stocktrack.dsl.stubForSucceededFindCommand
import com.otus.otuskotlin.stocktrack.dsl.stubForSucceededUpdateCommand
import com.otus.otuskotlin.stocktrack.dsl.stubs
import com.otus.otuskotlin.stocktrack.dsl.validateIdProperty
import com.otus.otuskotlin.stocktrack.dsl.validateNameProperty
import com.otus.otuskotlin.stocktrack.dsl.validateStockCategoryProperty
import com.otus.otuskotlin.stocktrack.dsl.validation
import com.otus.otuskotlin.stocktrack.model.Command

class SingleStockResponseProcessor(val coreSettings: CoreSettings) {

    suspend fun execute(context: SingleStockResponseContext): SingleStockResponseContext {
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

