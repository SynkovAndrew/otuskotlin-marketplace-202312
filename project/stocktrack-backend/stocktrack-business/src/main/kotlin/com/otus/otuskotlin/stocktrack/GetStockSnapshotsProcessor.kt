package com.otus.otuskotlin.stocktrack

import com.otus.otuskotlin.stocktrack.context.GetStockSnapshotsContext
import com.otus.otuskotlin.stocktrack.cor.chainBuilder
import com.otus.otuskotlin.stocktrack.dsl.command.commandPipeline
import com.otus.otuskotlin.stocktrack.dsl.command.findSnapshotsCommand
import com.otus.otuskotlin.stocktrack.dsl.command.predictSnapshotsCommand
import com.otus.otuskotlin.stocktrack.dsl.command.startProcessing
import com.otus.otuskotlin.stocktrack.model.Command

class GetStockSnapshotsProcessor(val coreSettings: CoreSettings) : ResponseProcessor<GetStockSnapshotsContext> {
    override suspend fun execute(context: GetStockSnapshotsContext): GetStockSnapshotsContext {
        return chainBuilder {
            startProcessing()

            commandPipeline(Command.FIND_SNAPSHOTS) {
                findSnapshotsCommand(coreSettings)
            }

            commandPipeline(Command.PREDICT_SNAPSHOTS) {
                predictSnapshotsCommand(coreSettings)
            }
        }.execute(context)
    }
}