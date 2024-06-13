package com.otus.otuskotlin.stocktrack

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.stock.StockFilterRepositoryRequest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class StockSearchProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<StockEntity>
) {
    fun search(filter: StockFilterRepositoryRequest): CompletionStage<Collection<StockEntity>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (!filter.name.isNullOrBlank()) {
            select = select
                .whereColumn(StockEntity.COLUMN_NAME)
                .like(QueryBuilder.literal("%${filter.name}%"))
        }
        if (filter.category != Stock.Category.NONE) {
            select = select
                .whereColumn(StockEntity.COLUMN_CATEGORY)
                .isEqualTo(QueryBuilder.literal(filter.category))
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<StockEntity>()
        private val future = CompletableFuture<Collection<StockEntity>>()
        val stage: CompletionStage<Collection<StockEntity>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }
}