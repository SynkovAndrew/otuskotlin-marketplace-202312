package com.otus.otuskotlin.marketplace

expect class UserService() {
    fun serve(user: User): String
}