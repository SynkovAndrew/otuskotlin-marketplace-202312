package com.otus.otuskotlin.lesson

import com.otus.otuskotlin.lesson.User
import kotlinx.coroutines.delay

actual class UserLongService actual constructor() {
    actual suspend fun serve(user: User): Pair<String, User> {
        delay(3000)
        return "JS PLATFORM" to user
    }
}