package com.otus.otuskotlin.marketplace

actual class UserAsyncService actual constructor() {
    actual suspend fun serve(user: User): Pair<String, User> {
        return "JVM PLATFORM" to user
    }
}