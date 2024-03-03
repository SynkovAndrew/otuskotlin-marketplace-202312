package com.otus.otuskotlin.marketplace

expect class UserAsyncService() {
    suspend fun serve(user: User): Pair<String, User>
}