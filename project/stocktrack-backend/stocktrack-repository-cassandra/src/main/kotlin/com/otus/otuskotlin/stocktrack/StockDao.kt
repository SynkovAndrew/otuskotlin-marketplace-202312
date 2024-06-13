package com.otus.otuskotlin.stocktrack

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.annotations.Dao
import com.datastax.oss.driver.api.mapper.annotations.Delete
import com.datastax.oss.driver.api.mapper.annotations.Insert
import com.datastax.oss.driver.api.mapper.annotations.Select
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes
import com.datastax.oss.driver.api.mapper.annotations.Update
import java.util.concurrent.CompletionStage

@Dao
interface StockDao {
    @Insert
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun create(entity: StockEntity): CompletionStage<Unit>

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun findById(id: String): CompletionStage<StockEntity?>

    @Update(customIfClause = "lock = :previousLock")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun update(entity: StockEntity, previousLock: String): CompletionStage<AsyncResultSet>

    @Delete(
        customWhereClause = "id = :id",
        customIfClause = "lock = :previousLock",
        entityClass = [StockEntity::class]
    )
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun delete(id: String, previousLock: String): CompletionStage<AsyncResultSet>
}