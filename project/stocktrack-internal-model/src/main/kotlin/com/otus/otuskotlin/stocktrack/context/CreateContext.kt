package com.otus.otuskotlin.stocktrack.context

import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.Error
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.model.Stock
import com.otus.otuskotlin.stocktrack.stub.Stub

class CreateContext(
    override val command: Command = Command.CREATE,
    val request: Stock = Stock.NONE,
    val response: Stock = Stock.NONE,
    state: State = State.NONE,
    errors: Set<Error> = emptySet(),
    debugMode: Debug.Mode = Debug.Mode.NONE,
    stub: Stub = Stub.NONE
) : Context(state, errors, debugMode, stub), CommandContext