package com.otus.otuskotlin.stocktrack

import kotlinx.serialization.Serializable

@Serializable
data class LogData(
    val level: LogLevel,
    val message: String,
//    val data: T
)
