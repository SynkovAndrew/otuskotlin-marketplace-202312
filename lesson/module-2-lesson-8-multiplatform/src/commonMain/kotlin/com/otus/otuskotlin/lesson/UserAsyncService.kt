package com.otus.otuskotlin.lesson

expect class UserAsyncService() {
    suspend fun serve(user: User): Pair<String, User>
}