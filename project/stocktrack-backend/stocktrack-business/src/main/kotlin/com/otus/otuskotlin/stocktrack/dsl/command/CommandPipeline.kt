package com.otus.otuskotlin.stocktrack.dsl.command

import com.otus.otuskotlin.stocktrack.context.Context
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.State

fun <T : Context<*, *>> ChainDsl<T>.commandPipeline(
    command: Command,
    block: ChainDsl<T>.() -> Unit
) {
    chain {
        block()
        invokeOn { it.state == State.RUNNING && it.command == command }
    }
}