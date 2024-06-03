package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.chainBuilder
import com.otus.otuskotlin.stocktrack.dsl.command.commandPipeline
import com.otus.otuskotlin.stocktrack.dsl.command.createCommand
import com.otus.otuskotlin.stocktrack.dsl.command.deleteCommand
import com.otus.otuskotlin.stocktrack.dsl.command.findCommand
import com.otus.otuskotlin.stocktrack.dsl.command.startProcessing
import com.otus.otuskotlin.stocktrack.dsl.command.updateCommand
import com.otus.otuskotlin.stocktrack.dsl.stub.stubForDbErrorOnCommand
import com.otus.otuskotlin.stocktrack.dsl.stub.stubForFailedCauseBadRequest
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

class SingleStockResponseProcessor(
    val coreSettings: CoreSettings
) : ResponseProcessor<SingleStockResponseContext> {

    override suspend fun execute(context: SingleStockResponseContext): SingleStockResponseContext {
        return chainBuilder {
            startProcessing()

            commandPipeline(Command.CREATE) {
                stubs {
                    stubForSucceededCreateCommand(coreSettings)
                    stubForFailedCauseBadRequest()
                    stubForDbErrorOnCommand()
                    stubForRequestedStubNotFound()
                }
                validation {
                    validateNameProperty()
                    validateStockCategoryProperty()
                }

                createCommand(coreSettings)
            }

            commandPipeline(Command.UPDATE) {
                stubs {
                    stubForSucceededUpdateCommand(coreSettings)
                    stubForFailedCauseBadRequest()
                    stubForDbErrorOnCommand()
                    stubForRequestedStubNotFound()
                }
                validation {
                    validateIdProperty()
                    validateNameProperty()
                    validateStockCategoryProperty()
                }

                updateCommand(coreSettings)
            }

            commandPipeline(Command.FIND) {
                stubs {
                    stubForSucceededFindCommand(coreSettings)
                    stubForFailedCauseBadRequest()
                    stubForDbErrorOnCommand()
                    stubForRequestedStubNotFound()
                }
                validation {
                    validateIdProperty()
                }

                findCommand(coreSettings)
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

                deleteCommand(coreSettings)
            }
        }.execute(context)
    }
}

