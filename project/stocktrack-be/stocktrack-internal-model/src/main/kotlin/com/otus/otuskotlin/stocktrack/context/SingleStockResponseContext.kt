package com.otus.otuskotlin.stocktrack.context

import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.Error
import com.otus.otuskotlin.stocktrack.model.RequestId
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.model.Stock
import java.time.Instant
import java.util.*

data class SingleStockResponseContext(
    override val command: Command,
    override val request: Stock = Stock.NONE,
    override val response: Stock = Stock.NONE,
    override val state: State = State.NONE,
    override val errors: List<Error> = emptyList(),
    override val debug: Debug = Debug.NONE,
    override val requestId: RequestId = RequestId(value = UUID.randomUUID().toString()),
    override val startedAt: Instant = Instant.MIN
) : Context<Stock, Stock>