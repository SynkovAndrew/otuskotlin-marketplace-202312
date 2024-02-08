package com.otus.otuskotlin.marketplace

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class Playing {

    @Test
    fun coroutine() {
        runBlocking {
            print()
        }
    }

    private suspend fun print() {
        coroutineScope {
            launch {
                delay(2000L)
                println("World2")
            }
            launch {
                delay(1000L)
                println("World1")
            }
            println("Hello")
        }
    }
}