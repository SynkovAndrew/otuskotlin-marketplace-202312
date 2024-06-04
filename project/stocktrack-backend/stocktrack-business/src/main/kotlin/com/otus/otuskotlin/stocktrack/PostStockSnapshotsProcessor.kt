package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.GetStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.context.PostStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.cor.chainBuilder
import com.otus.otuskotlin.stocktrack.dsl.command.commandPipeline
import com.otus.otuskotlin.stocktrack.dsl.command.findSnapshotsCommand
import com.otus.otuskotlin.stocktrack.dsl.command.predictSnapshotsCommand
import com.otus.otuskotlin.stocktrack.dsl.command.startProcessing
import com.otus.otuskotlin.stocktrack.dsl.command.uploadSnapshotsCommand
import com.otus.otuskotlin.stocktrack.model.Command

class PostStockSnapshotsProcessor(val coreSettings: CoreSettings) : ResponseProcessor<PostStockSnapshotsContext> {
    override suspend fun execute(context: PostStockSnapshotsContext): PostStockSnapshotsContext {
        return chainBuilder {
            startProcessing()

            commandPipeline(Command.UPLOAD_SNAPSHOTS) {
                uploadSnapshotsCommand(coreSettings)
            }

        }.execute(context)
    }
}