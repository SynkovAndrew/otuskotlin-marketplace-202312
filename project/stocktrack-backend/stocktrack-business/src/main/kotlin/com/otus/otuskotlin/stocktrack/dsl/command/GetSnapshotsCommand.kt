package com.otus.otuskotlin.stocktrack.dsl.command

import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.context.GetStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State

fun ChainDsl<GetStockSnapshotsContext>.findSnapshotsCommand(
    coreSettings: CoreSettings
) {
    processor {
        this.name = Command.FIND_SNAPSHOTS.name

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.FIND_SNAPSHOTS
        }

        process {
            it
        }

        handleException { throwable, context ->
            context.copy(
                errors = context.errors + ErrorDescription(
                    message = throwable.message ?: "",
                    throwable = throwable
                )
            )
        }
    }
}