package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.api.v1.models.CreateStockRequest
import com.otus.otuskotlin.stocktrack.api.v1.models.Debug
import com.otus.otuskotlin.stocktrack.context.CreateContext
import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.model.Debug as DebugInternalModel

fun CreateStockRequest.toContext(): CreateContext {
    return CreateContext(
        request = Stock(
            name = stock.name,
            category = StockCategoryMapper.fromApiModel(stock.category),
        ),
        response = Stock.NONE
    )
}

fun Debug.toInternalModel(): DebugInternalModel {
    return DebugInternalModel(

    )
}
