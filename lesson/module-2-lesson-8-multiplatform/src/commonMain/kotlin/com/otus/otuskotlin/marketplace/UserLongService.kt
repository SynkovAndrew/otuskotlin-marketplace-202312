package com.otus.otuskotlin.marketplace

expect class UserLongService() {
    suspend fun serve(user: User): Pair<String, User>
}