package com.otus.otuskotlin.lesson

import com.otus.otuskotlin.lesson.User

actual class UserService actual constructor() {
    actual fun serve(user: User): String {
        return "JS PLATFORM"
    }
}