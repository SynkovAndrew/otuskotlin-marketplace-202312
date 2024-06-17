package com.otus.otuskotlin.stocktrack.dsl.command

import com.otus.otuskotlin.stocktrack.CoreSettings
import com.otus.otuskotlin.stocktrack.context.PostStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.cor.ChainDsl
import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.State

fun ChainDsl<PostStockSnapshotsContext>.uploadSnapshotsCommand(
    coreSettings: CoreSettings
) {
    processor {
        this.name = Command.UPLOAD_SNAPSHOTS.name

        invokeOn {
            it.state == State.RUNNING &&
                    it.command == Command.UPLOAD_SNAPSHOTS
        }

        process {
            it.request
                .map { snapshot -> coreSettings.stockSnapshotRepository.create(snapshot) }
                .let { _ -> it.copy(response = Unit) }
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