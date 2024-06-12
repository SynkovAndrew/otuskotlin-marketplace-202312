package com.otus.otuskotlin.stocktrack

data class CassandraProperties(
    val host: String = "localhost",
    val port: Int = 9042,
    val user: String = "cassandra",
    val password: String = "cassandra",
    val keyspace: String = "keyspace"
)