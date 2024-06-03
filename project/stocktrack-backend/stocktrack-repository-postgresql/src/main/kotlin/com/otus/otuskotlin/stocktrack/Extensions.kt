package com.otus.otuskotlin.stocktrack

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

suspend inline fun <T> transactionWrapper(
    database: Database,
    crossinline block: () -> T
): T {
    return withContext(Dispatchers.IO) {
        transaction(database) {
            block()
        }
    }
}