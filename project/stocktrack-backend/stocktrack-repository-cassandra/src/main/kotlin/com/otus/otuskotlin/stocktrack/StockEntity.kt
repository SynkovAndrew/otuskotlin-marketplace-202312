package com.otus.otuskotlin.stocktrack

import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.stock.StockLock

@Entity
data class StockEntity(
    @CqlName(COLUMN_ID)
    @PartitionKey
    val id: String,

    @CqlName(COLUMN_NAME)
    val name: String,

    @CqlName(COLUMN_CATEGORY)
    val category: String,

    @CqlName(COLUMN_LOCK)
    val lock: String
) {

    fun toInternal(): Stock {
        return Stock(
            id = Stock.Id(value = id),
            name = name,
            category = Stock.Category.valueOf(category),
            lock = StockLock(value = lock)
        )
    }

    companion object {
        fun fromInternal(stock: Stock): StockEntity {
            return StockEntity(
                id = stock.id.value,
                name = stock.name,
                category = stock.category.name,
                lock = stock.lock.value
            )
        }

        fun table(keyspace: String, tableName: String) {
            SchemaBuilder
                .createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey(COLUMN_ID, DataTypes.TEXT)
                .withColumn(COLUMN_NAME, DataTypes.TEXT)
                .withColumn(COLUMN_CATEGORY, DataTypes.TEXT)
                .withColumn(COLUMN_LOCK, DataTypes.TEXT)
                .build()
        }

        fun titleIndex(keyspace: String, tableName: String, locale: String = "en") {
            SchemaBuilder
                .createIndex()
                .ifNotExists()
                .usingSASI()
                .onTable(keyspace, tableName)
                .andColumn(COLUMN_NAME)
                .withSASIOptions(mapOf("mode" to "CONTAINS", "tokenization_locale" to locale))
                .build()
        }

        const val TABLE_NAME = "stock"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_LOCK = "lock"
    }
}