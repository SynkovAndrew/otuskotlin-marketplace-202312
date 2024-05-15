package com.otus.otuskotlin.stocktrack.debug

import com.otus.otuskotlin.stocktrack.Mapper
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugMode
import com.otus.otuskotlin.stocktrack.model.Debug

object DebugModeMapper : Mapper<Debug.Mode, DebugMode> {
    override fun toTransportModel(internalModel: Debug.Mode): DebugMode {
        return when(internalModel) {
            Debug.Mode.PROD -> DebugMode.PROD
            Debug.Mode.TEST -> DebugMode.TEST
            Debug.Mode.STUB -> DebugMode.STUB
            Debug.Mode.NONE -> throw IllegalArgumentException(
                "There is no such a value $internalModel in DebugMode"
            )
        }
    }

    override fun fromTransportModel(apiModel: DebugMode): Debug.Mode {
        return when(apiModel) {
            DebugMode.PROD -> Debug.Mode.PROD
            DebugMode.TEST -> Debug.Mode.TEST
            DebugMode.STUB -> Debug.Mode.STUB
        }
    }
}