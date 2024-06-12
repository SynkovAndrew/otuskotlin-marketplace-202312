package com.otus.otuskotlin.stocktrack

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.ResultSet
import com.datastax.oss.driver.api.core.cql.Row
import com.otus.otuskotlin.stocktrack.stock.BaseStockRepository
import com.otus.otuskotlin.stocktrack.stock.Stock
import com.otus.otuskotlin.stocktrack.stock.StockFilterRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockIdRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryRequest
import com.otus.otuskotlin.stocktrack.stock.StockRepositoryResponse
import com.otus.otuskotlin.stocktrack.stock.StocksRepositoryResponse
import java.net.InetAddress
import java.net.InetSocketAddress

class CassandraStockRepository(private val cassandraProperties: CassandraProperties) : BaseStockRepository() {
    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(
                listOf(
                    InetSocketAddress(
                        InetAddress.getByName(cassandraProperties.host),
                        cassandraProperties.port
                    )
                )
            )
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(cassandraProperties.user, cassandraProperties.password)
            .build()
    }

    fun test() {
        val rs: ResultSet = session.execute("select release_version from system.local") // (2)
        val row: Row? = rs.one()

        println(row?.getString("release_version"))
    }

    override fun enrich(stocks: Collection<Stock>): Collection<Stock> {
        TODO("Not yet implemented")
    }

    override suspend fun create(request: StockRepositoryRequest): StockRepositoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun findById(request: StockIdRepositoryRequest): StockRepositoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun update(request: StockRepositoryRequest): StockRepositoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun delete(request: StockIdRepositoryRequest): StockRepositoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun search(request: StockFilterRepositoryRequest): StocksRepositoryResponse {
        TODO("Not yet implemented")
    }
}