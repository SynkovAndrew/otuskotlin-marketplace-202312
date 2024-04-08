package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.Mapper
import com.otus.otuskotlin.stocktrack.api.v1.models.StockCategory
import com.otus.otuskotlin.stocktrack.model.Stock

object StockCategoryMapper : Mapper<Stock.Category, StockCategory> {
    override fun toTransportModel(internalModel: Stock.Category): StockCategory {
        return when(internalModel) {
            Stock.Category.BOND -> StockCategory.BOND
            Stock.Category.SHARE -> StockCategory.SHARE
            Stock.Category.NONE -> throw IllegalArgumentException(
                "There is no such a value $internalModel in StockCategory"
            )
        }
    }

    override fun fromTransportModel(apiModel: StockCategory): Stock.Category {
        return when(apiModel) {
            StockCategory.BOND -> Stock.Category.BOND
            StockCategory.SHARE -> Stock.Category.SHARE
        }
    }
}