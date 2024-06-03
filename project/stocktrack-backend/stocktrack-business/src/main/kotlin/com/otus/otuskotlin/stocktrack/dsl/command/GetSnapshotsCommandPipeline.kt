package com.otus.otuskotlin.stocktrack.dsl.command

import com.otus.otuskotlin.stocktrack.context.GetStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.State

fun ChainDsl<GetStockSnapshotsContext>.commandPipeline(
    command: Command,
    block: ChainDsl<GetStockSnapshotsContext>.() -> Unit
) {
    chain {
        block()
        invokeOn { it.state == State.RUNNING && it.command == command }
    }
}