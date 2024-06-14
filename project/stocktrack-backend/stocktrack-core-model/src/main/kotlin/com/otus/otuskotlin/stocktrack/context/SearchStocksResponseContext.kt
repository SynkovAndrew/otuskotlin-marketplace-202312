package com.otus.otuskotlin.stocktrack.context

import com.otus.otuskotlin.stocktrack.model.Command
import com.otus.otuskotlin.stocktrack.model.Debug
import com.otus.otuskotlin.stocktrack.model.ErrorDescription
import com.otus.otuskotlin.stocktrack.model.RequestId
import com.otus.otuskotlin.stocktrack.model.State
import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.stock.StockFilter
import java.time.Instant
import java.util.UUID

data class SearchStocksResponseContext(
    override val command: Command,
    override val request: StockFilter = StockFilter(),
    override val response: List<Stock> = emptyList(),
    override val state: State = State.NONE,
    override val errors: List<ErrorDescription> = emptyList(),
    override val debug: Debug = Debug.NONE,
    override val requestId: RequestId = RequestId(value = UUID.randomUUID().toString()),
    override val startedAt: Instant = Instant.MIN
) : Context<StockFilter, List<Stock>, SearchStocksResponseContext> {
    override fun start(): SearchStocksResponseContext {
        return copy(
            startedAt = Instant.now(),
            state = State.RUNNING
        )
    }

    override fun fail(error: ErrorDescription): SearchStocksResponseContext {
        return copy(
            state = State.FAILED,
            errors = errors + error
        )
    }

    override fun fail(error: Collection<ErrorDescription>): SearchStocksResponseContext {
        return copy(
            state = State.FAILED,
            errors = errors + error
        )
    }

    override fun finish(): SearchStocksResponseContext {
        return copy(state = State.FINISHED)
    }
}