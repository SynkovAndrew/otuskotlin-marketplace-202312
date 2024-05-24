package com.otus.otuskotlin.stocktrack.dsl

import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.model.Stock

fun ChainDsl<SingleStockResponseContext>.validateCreateCommandNameProperty() {
    processor {
        this.name = "validateCreateCommandNameProperty"

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.CREATE &&
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

fun ChainDsl<SingleStockResponseContext>.validateCreateCommandIdProperty() {
    processor {
        this.name = "validateCreateCommandIdProperty"

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.CREATE &&
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

fun ChainDsl<SingleStockResponseContext>.validateCreateCommandCategoryProperty() {
    processor {
        this.name = "validateCreateCommandCategoryProperty"

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.CREATE &&
                    it.request.category == Stock.Category.NONE
        }

        process {
            it.copy(
                state = State.FAILED,
                errors = it.errors + ErrorDescription(
                    code = "CATEGORY_EMPTY",
                    field = "category",
                    group = "validation",
                    message = "Category is empty",
                )
            )
        }
    }
}