package com.otus.otuskotlin.stocktrack.context

import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
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
    override val errors: List<ErrorDescription> = emptyList(),
    override val debug: Debug = Debug.NONE,
    override val requestId: RequestId = RequestId(value = UUID.randomUUID().toString()),
    override val startedAt: Instant = Instant.MIN
) : Context<Stock, Stock> {
    override fun start(): Context<Stock, Stock> {
        return copy(
            startedAt = Instant.now(),
            state = State.RUNNING
        )
    }

    override fun fail(error: ErrorDescription): Context<Stock, Stock> {
        return copy(
            state = State.FAILED,
            errors = errors + error
        )
    }

    override fun finish(): Context<Stock, Stock> {
        return copy(state = State.FINISHED)
    }
}