package com.otus.otuskotlin.stocktrack.debug

import com.otus.otuskotlin.stocktrack.Mapper
import com.otus.otuskotlin.stocktrack.api.v1.models.DebugStub
import com.otus.otuskotlin.stocktrack.model.Debug

object DebugStubMapper : Mapper<Debug.Stub, DebugStub> {
    override fun toTransportModel(internalModel: Debug.Stub): DebugStub {
        return when (internalModel) {
            Debug.Stub.SUCCESS -> DebugStub.SUCCESS
            Debug.Stub.NOT_FOUND -> DebugStub.NOT_FOUND
            Debug.Stub.BAD_REQUEST -> DebugStub.BAD_REQUEST
            Debug.Stub.DATABASE_ERROR -> DebugStub.DATABASE_ERROR
            Debug.Stub.NONE -> throw IllegalArgumentException(
                "There is no such a value $internalModel in DebugStub"
            )
        }
    }

    override fun fromTransportModel(apiModel: DebugStub): Debug.Stub {
        return when(apiModel) {
            DebugStub.SUCCESS -> Debug.Stub.SUCCESS
            DebugStub.NOT_FOUND -> Debug.Stub.NOT_FOUND
            DebugStub.BAD_REQUEST -> Debug.Stub.BAD_REQUEST
            DebugStub.DATABASE_ERROR -> Debug.Stub.DATABASE_ERROR
        }
    }
}
