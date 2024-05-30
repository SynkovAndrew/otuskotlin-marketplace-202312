package com.otus.otuskotlin.stocktrack.dsl.validation

import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.State

fun <T : Context<*, *, T>> ChainDsl<T>.validation(
    block: ChainDsl<T>.() -> Unit
) {
    chain {
        block()
        invokeOn { it.state == State.RUNNING }
    }
}