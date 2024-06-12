package com.otus.otuskotlin.stocktrack

import kotlin.test.Test

class CassandraStockRepositoryTest {
    private val repository = CassandraStockRepository(CassandraProperties())

    @Test
    fun test() {
        repository.test()
    }
}