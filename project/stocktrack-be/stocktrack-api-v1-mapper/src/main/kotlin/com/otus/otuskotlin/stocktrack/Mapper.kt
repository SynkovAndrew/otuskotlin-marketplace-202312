package com.otus.otuskotlin.stocktrack

interface Mapper<INTERNAL_MODEL, TRANSPORT_MODEL> {
    fun toTransportModel(internalModel: INTERNAL_MODEL): TRANSPORT_MODEL

    fun fromTransportModel(apiModel: TRANSPORT_MODEL): INTERNAL_MODEL

    fun toTransportModels(internalModels: Iterable<INTERNAL_MODEL>): List<TRANSPORT_MODEL> {
        return internalModels.map { toTransportModel(it) }
    }

    fun fromTransportModels(apiModels: Iterable<TRANSPORT_MODEL>): List<INTERNAL_MODEL> {
        return apiModels.map { fromTransportModel(it) }
    }
}
