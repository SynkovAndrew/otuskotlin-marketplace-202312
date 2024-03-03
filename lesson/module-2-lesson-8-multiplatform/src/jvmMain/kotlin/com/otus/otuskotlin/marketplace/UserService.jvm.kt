package com.otus.otuskotlin.marketplace

actual class UserService actual constructor() {
    actual fun serve(user: User): String {
        return "JVM PLATFORM"
    }
}