package com.otus.otuskotlin.lesson

expect class UserLongService() {
    suspend fun serve(user: User): Pair<String, User>
}