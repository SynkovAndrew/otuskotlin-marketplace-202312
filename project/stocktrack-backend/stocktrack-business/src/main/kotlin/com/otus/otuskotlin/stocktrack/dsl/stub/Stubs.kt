package com.otus.otuskotlin.stocktrack.dsl.stub

import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.State

fun <T : Context<*, *, T>> ChainDsl<T>.stubs(
    block: ChainDsl<T>.() -> Unit
) {
    chain {
        block()
        invokeOn { it.state == State.RUNNING && it.debug.mode == Debug.Mode.STUB }
    }
}