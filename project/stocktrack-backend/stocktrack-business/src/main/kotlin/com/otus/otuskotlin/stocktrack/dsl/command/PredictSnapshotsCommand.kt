package com.otus.otuskotlin.stocktrack.dsl.command

import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.context.GetStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State

fun ChainDsl<GetStockSnapshotsContext>.predictSnapshotsCommand(
    coreSettings: CoreSettings
) {
    processor {
        this.name = Command.PREDICT_SNAPSHOTS.name

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.PREDICT_SNAPSHOTS
        }

        process {
            coreSettings.stockSnapshotRepository.findByStockId(it.request)
                .let { response -> it.handleResponse(response) }
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