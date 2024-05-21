package com.otus.otuskotlin.stocktrack.cor

interface Executor<T> {
    suspend fun execute(context: T): T
}