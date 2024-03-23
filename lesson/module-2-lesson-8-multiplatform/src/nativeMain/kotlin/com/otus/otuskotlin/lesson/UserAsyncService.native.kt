package com.otus.otuskotlin.lesson

import com.otus.otuskotlin.lesson.User

actual class UserAsyncService actual constructor() {
    actual suspend fun serve(user: User): Pair<String, User> {
        return "JS Native" to user
    }
}