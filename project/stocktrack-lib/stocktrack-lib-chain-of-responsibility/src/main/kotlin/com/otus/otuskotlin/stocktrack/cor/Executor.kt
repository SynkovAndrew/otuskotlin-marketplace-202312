package com.otus.otuskotlin.stocktrack.cor

interface Executor<T> {
    fun invokeOn(context: T): Boolean

    suspend fun execute(context: T): T
}