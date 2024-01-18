package com.otus.otuskotlin.marketplace

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertFails

class Exercise2Types {
    @Test
    fun resFun() {
        unitRes()

        assertEquals(220, intRes())
    }

    @Test
    fun nothingFun() {
        assertFails {
            nothingRes()
        }

        assertEquals(1, withNothing(12))

        assertFails {
            withNothing(13)
        }
    }
}

private fun unitRes(): Unit = println("Result is unit")

private fun intRes(): Int = 22 * 10

private fun nothingRes(): Nothing = throw Exception("My Exception")

private fun withNothing(i: Int): Int = when (i) {
    12 -> 1
    else -> nothingRes()
}