package com.otus.otuskotlin.stocktrack.stock

import com.otus.otuskotlin.stocktrack.Mapper
import com.otus.otuskotlin.stocktrack.api.v1.models.StockPermission
import com.otus.otuskotlin.stocktrack.stock.StockPermission as InternalStockPermission

object StockPermissionMapper : Mapper<InternalStockPermission, StockPermission> {
    override fun toTransportModel(internalModel: InternalStockPermission): StockPermission {
        return when(internalModel) {
            InternalStockPermission.READ -> StockPermission.READ
            InternalStockPermission.WRITE -> StockPermission.WRITE
        }
    }

    override fun fromTransportModel(apiModel: StockPermission): InternalStockPermission {
        return when(apiModel) {
            StockPermission.READ -> InternalStockPermission.READ
            StockPermission.WRITE -> InternalStockPermission.WRITE
        }
    }
}
