package com.otus.otuskotlin.lesson

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Exercise2ArraysTest {
    @Test
    fun arrays() {
        val arrayOfInt = arrayOf(1, 2, 3)
        println("Unreadable content: $arrayOfInt")
        println(arrayOfInt.contentToString())

        val emptyArray = emptyArray<Int>() // arrayOf()
        assertTrue(emptyArray.isEmpty())

        val arrayCalculated = Array(5) { i -> i + 1 }
        println("Computed content: ${arrayCalculated.contentToString()}")

        val intArray = intArrayOf(1, 2, 3) // int[]
        assertFalse(intArray.isEmpty())

        val element = arrayOfInt[2] // 3
        val elementByFunction = arrayOfInt.get(2) // 3
        assertEquals(element, elementByFunction)
    }
}