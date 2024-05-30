package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.chainBuilder
import com.otus.otuskotlin.stocktrack.dsl.command.command
import com.otus.otuskotlin.stocktrack.dsl.command.commandPipeline
import com.otus.otuskotlin.stocktrack.dsl.command.startProcessing
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
import com.otus.otuskotlin.stocktrack.stock.ErrorStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.OkWithErrorsStockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.StockIdRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest

class SingleStockResponseProcessor(
    val coreSettings: CoreSettings
) : ResponseProcessor<SingleStockResponseContext> {

    override suspend fun execute(context: SingleStockResponseContext): SingleStockResponseContext {
        return chainBuilder<SingleStockResponseContext> {
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

                command(Command.CREATE) {
                    StockRepositoryRequest(stock = request)
                        .let { request -> coreSettings.prodStockRepository.create(request) }
                        .let { response ->
                            when (response) {
                                is OkStockRepositoryResponse -> copy(response = response.data).finish()
                                is ErrorStockRepositoryResponse -> fail(response.errors)
                                is OkWithErrorsStockRepositoryResponse -> copy(response = response.data)
                                    .fail(response.errors)
                            }
                        }
                }
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

                command(Command.UPDATE) {
                    StockRepositoryRequest(stock = request)
                        .let { request -> coreSettings.prodStockRepository.update(request) }
                        .let { response ->
                            when (response) {
                                is OkStockRepositoryResponse -> copy(response = response.data).finish()
                                is ErrorStockRepositoryResponse -> fail(response.errors)
                                is OkWithErrorsStockRepositoryResponse -> copy(response = response.data)
                                    .fail(response.errors)
                            }
                        }
                }
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

                command(Command.FIND) {
                    StockIdRepositoryRequest(stockId = request.id)
                        .let { request -> coreSettings.prodStockRepository.findById(request) }
                        .let { response ->
                            when (response) {
                                is OkStockRepositoryResponse -> copy(response = response.data).finish()
                                is ErrorStockRepositoryResponse -> fail(response.errors)
                                is OkWithErrorsStockRepositoryResponse -> copy(response = response.data)
                                    .fail(response.errors)
                            }
                        }
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
                    StockIdRepositoryRequest(stockId = request.id)
                        .let { request -> coreSettings.prodStockRepository.delete(request) }
                        .let { response ->
                            when (response) {
                                is OkStockRepositoryResponse -> copy(response = response.data).finish()
                                is ErrorStockRepositoryResponse -> fail(response.errors)
                                is OkWithErrorsStockRepositoryResponse -> copy(response = response.data)
                                    .fail(response.errors)
                            }
                        }
                }
            }
        }.execute(context)
    }
}

