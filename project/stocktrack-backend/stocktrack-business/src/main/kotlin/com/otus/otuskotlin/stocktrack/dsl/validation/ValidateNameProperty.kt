package com.otus.otuskotlin.stocktrack.dsl.validation

import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State

fun ChainDsl<SingleStockResponseContext>.validateNameProperty() {
    processor {
        this.name = "validateNameProperty"

        invokeOn {
            it.state == State.RUNNING &&
                    it.request.name.trim().isEmpty()
        }

        process {
            it.copy(
                state = State.FAILED,
                errors = it.errors + ErrorDescription(
                    code = "NAME_EMPTY",
                    field = "name",
                    group = "validation",
                    message = "Name is empty",
                )
            )
        }
    }
}