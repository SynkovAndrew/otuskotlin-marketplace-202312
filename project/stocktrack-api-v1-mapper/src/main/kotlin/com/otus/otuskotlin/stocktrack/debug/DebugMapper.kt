package com.otus.otuskotlin.stocktrack.debug

import com.otus.otuskotlin.stocktrack.Mapper
import com.otus.otuskotlin.stocktrack.api.v1.models.Debug
import com.otus.otuskotlin.stocktrack.model.Debug as DebugInternalModel

object DebugMapper : Mapper<DebugInternalModel, Debug> {
    override fun toTransportModel(internalModel: DebugInternalModel): Debug {
        return Debug(
            mode = DebugModeMapper.toTransportModel(internalModel.mode),
            stub = DebugStubMapper.toTransportModel(internalModel.stub)
        )
    }

    override fun fromTransportModel(apiModel: Debug): DebugInternalModel {
        return DebugInternalModel(
            mode = DebugModeMapper.fromTransportModel(apiModel.mode),
            stub = DebugStubMapper.fromTransportModel(apiModel.stub)
        )
    }
}
