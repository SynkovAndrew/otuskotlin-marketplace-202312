package com.otus.otuskotlin.marketplace

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class Exercise1Functions {

    @Test
    fun simpleFun() {
        val param = 0.1
        val expected = param * param
        assertEquals(expected, simple(param))
    }

    @Test
    fun defaultArgs() {
        assertEquals("str: 1, 12", defaultArgs(1))
    }

    @Test
    fun namedArgs() {
        val res = defaultArgs(s = "string", x = 8, y = 7)
        assertEquals("string: 8, 7", res)
    }

    @Test
    fun extensions() {
        assertEquals("My String is string", "string".myExtension())
    }
}

private fun simple(x: Double): Double = x * x

private fun defaultArgs(x: Int, y: Int = 12, s: String = "str") = "$s: $x, $y"

private fun String.myExtension() = "My String is $this"