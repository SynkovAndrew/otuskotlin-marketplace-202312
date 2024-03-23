package com.otus.otuskotlin.sample

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals


class TestTest {

    @Test
    fun test(): Unit = runBlocking {
        assertEquals(4, 2 + 2)
    }
}