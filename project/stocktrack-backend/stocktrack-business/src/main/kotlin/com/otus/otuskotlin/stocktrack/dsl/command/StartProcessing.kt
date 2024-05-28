package com.otus.otuskotlin.stocktrack.dsl.command

import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.State

fun <T : Context<*, *>> ChainDsl<T>.startProcessing() {
    processor {
        name = "startProcessing"
        invokeOn { it.state == State.NONE }
        process {
            it.start() as T
        }
    }
}