package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.Mapper
import com.otus.otuskotlin.stocktrack.api.v1.models.StockId
import com.otus.otuskotlin.stocktrack.model.Stock

object StockIdMapper : Mapper<Stock.Id, StockId> {
    override fun toTransportModel(internalModel: Stock.Id): StockId {
        return StockId(value = internalModel.value)
    }

    override fun fromTransportModel(apiModel: StockId): Stock.Id {
        return Stock.Id(value = apiModel.value)
    }
}
