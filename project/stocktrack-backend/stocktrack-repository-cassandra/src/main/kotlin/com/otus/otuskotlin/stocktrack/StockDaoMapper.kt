package com.otus.otuskotlin.stocktrack

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace
import com.datastax.oss.driver.api.mapper.annotations.DaoTable
import com.datastax.oss.driver.api.mapper.annotations.Mapper

@Mapper
interface StockDaoMapper {

    @DaoFactory
    fun stockDao(@DaoKeyspace keyspace: String, @DaoTable tableName: String): StockDao

    companion object {
        fun builder(session: CqlSession) = StockDaoMapperBuilder(session)
    }
}