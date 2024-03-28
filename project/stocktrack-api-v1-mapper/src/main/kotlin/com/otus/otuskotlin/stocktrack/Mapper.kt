package com.otus.otuskotlin.stocktrack

interface Mapper<INTERNAL_MODEL, API_MODEL> {
    fun toApiModel(internalModel: INTERNAL_MODEL): API_MODEL

    fun fromApiModel(apiModel: API_MODEL): INTERNAL_MODEL
}
