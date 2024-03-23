package com.otus.otuskotlin.lesson

expect class UserService() {
    fun serve(user: User): String
}