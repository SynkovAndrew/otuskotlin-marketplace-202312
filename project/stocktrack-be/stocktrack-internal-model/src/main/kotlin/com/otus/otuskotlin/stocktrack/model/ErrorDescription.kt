package com.otus.otuskotlin.stocktrack.model

data class ErrorDescription(
    val code: String,
    val group: String,
    val field: String,
    val message: String,
    val throwable: Throwable? = null
)
