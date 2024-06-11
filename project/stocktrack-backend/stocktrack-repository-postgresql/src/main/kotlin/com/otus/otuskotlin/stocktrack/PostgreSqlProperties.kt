package com.otus.otuskotlin.stocktrack

data class PostgreSqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "user",
    val password: String = "password",
    val database: String = "stocktrack-db",
    val schema: String = "public",
    val table: String = "stock",
    val driver: String = "org.postgresql.Driver",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
