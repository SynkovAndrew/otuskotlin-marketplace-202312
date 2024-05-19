package com.otus.otuskotlin.stocktrack.cor

interface Executor<T> {
    fun execute(context: T): T
}