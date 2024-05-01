package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.State

class SingleStockResponseProcessor(val coreSettings: CoreSettings) {

    suspend fun execute(context: SingleStockResponseContext): SingleStockResponseContext {

        return context.copy(
            state = State.RUNNING,
            response = when (context.command) {
                Command.CREATE -> context.request
                Command.UPDATE -> StubStockRepository.findById(context.request.id)
                    .copy(
                        name = context.request.name,
                        category = context.request.category
                    )

                Command.FIND,
                Command.DELETE -> StubStockRepository.findById(context.request.id)

                else -> throw IllegalArgumentException("Command ${context.command} not supported")
            }
        )
    }
}
