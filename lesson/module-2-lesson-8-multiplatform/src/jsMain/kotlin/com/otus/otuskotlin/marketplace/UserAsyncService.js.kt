package com.otus.otuskotlin.marketplace

actual class UserAsyncService actual constructor() {
    actual suspend fun serve(user: User): Pair<String, User> {
        return "JS PLATFORM" to user
    }
}