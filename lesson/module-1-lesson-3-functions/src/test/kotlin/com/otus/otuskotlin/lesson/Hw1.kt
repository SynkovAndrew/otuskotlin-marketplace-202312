package com.otus.otuskotlin.lesson

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class Hw1 {

    @Test
    fun mapListToNamesTest() {
        val input = listOf(
            mapOf(
                "first" to "Иван",
                "middle" to "Васильевич",
                "last" to "Рюрикович",
            ),
            mapOf(
                "first" to "Петька",
            ),
            mapOf(
                "first" to "Сергей",
                "last" to "Королев",
            ),
        )
        val expected = listOf(
            "Рюрикович Иван Васильевич",
            "Петька",
            "Королев Сергей",
        )
        val res = mapListToNames(input)
        assertEquals(expected, res)
    }

    private fun mapListToNames(input: List<Map<String, String>>): List<String> {
        return input.map {
            it.entries
                .sortedBy { (key, _) -> key.sortOrder() }
                .joinToString(separator = " ") { (_, value) -> value }
        }
    }

    private fun String.sortOrder(): Int {
        return when (this) {
            "first" -> 2
            "middle" -> 3
            "last" -> 1
            else -> 0
        }
    }
}