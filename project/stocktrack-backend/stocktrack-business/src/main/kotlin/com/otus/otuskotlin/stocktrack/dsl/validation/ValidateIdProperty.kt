package com.otus.otuskotlin.stocktrack.dsl.validation

import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State

fun ChainDsl<SingleStockResponseContext>.validateIdProperty() {
    processor {
        this.name = "validateIdProperty"

        invokeOn {
            it.state == State.RUNNING &&
                    it.request.id.value.trim().isEmpty()
        }

        process {
            it.copy(
                state = State.FAILED,
                errors = it.errors + ErrorDescription(
                    code = "ID_EMPTY",
                    field = "id",
                    group = "validation",
                    message = "Id is empty",
                )
            )
        }
    }
}