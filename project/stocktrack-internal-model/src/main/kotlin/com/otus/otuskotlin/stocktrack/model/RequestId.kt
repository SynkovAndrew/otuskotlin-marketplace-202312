package com.otus.otuskotlin.stocktrack.model

@JvmInline
value class RequestId(private val value: String) {
    fun asString(): String = value
}