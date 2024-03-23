package com.otus.otuskotlin.lesson

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class Exercise4Infix {
    @Test
    fun test() {
        assertEquals(17, 10 add 7)
    }

    private infix fun Int.add(n: Int): Int = this + n
}