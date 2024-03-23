package com.otus.otuskotlin.lesson

import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class DslTest {

    @Test
    fun test() {
        fun basic(block: () -> Int): String {
            return block().toString()
        }

        class Context {
            fun time(): LocalDateTime = LocalDateTime.now()
        }

        fun withContext(block: Context.() -> String): String {
            return Context().block()
        }

        val basic = basic { 10 }
        assertEquals(basic, "10")

        val withContext = withContext {
            "Year: " + time().year
        }
        assertEquals(withContext, "Year: 2024")
    }

    @Test
    fun dataTest() {
        val data = data {
            name = "Frog"
            number = 1111
        }

        assertEquals(data, Data(1111, "Frog"))
    }


    data class Data(
        val number: Int,
        val name: String
    )

    class DataGenerator {
        var number: Int = 0
        var name: String = ""

        fun withNumber(value: Int): DataGenerator {
            return this.also { number = value }
        }

        fun withName(value: String): DataGenerator {
            return this.also { name = value }
        }

        fun get(): Data {
            return Data(number, name)
        }
    }

    private fun data(block: DataGenerator.() -> Unit): Data {
        return DataGenerator().apply(block).get()
    }
}