package com.otus.otuskotlin.stocktrack.dsl.validation

import com.otus.otuskotlin.stocktrack.context.SingleStockResponseContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.stock.Stock

fun ChainDsl<SingleStockResponseContext>.validateStockCategoryProperty() {
    processor {
        this.name = "validateStockCategoryProperty"

        invokeOn {
            it.state == State.RUNNING &&
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