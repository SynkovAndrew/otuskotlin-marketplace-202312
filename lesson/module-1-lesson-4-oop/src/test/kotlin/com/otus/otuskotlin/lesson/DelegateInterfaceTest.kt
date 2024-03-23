package com.otus.otuskotlin.lesson

import kotlin.test.Test
import kotlin.test.assertEquals

class DelegateInterfaceTest {

    @Test
    fun test() {
        val converter = ConverterImpl()
        val derived = Derived(converter)

        assertEquals("33", derived.convert(33))
    }

    interface Converter {
        val name: String

        fun convert(integer: Int): String
    }

    class ConverterImpl : Converter {
        override val name: String = "Tratatta"

        override fun convert(integer: Int): String {
            return integer.toString()
        }
    }

    class Derived(private val passedConverter: Converter) : Converter by passedConverter {

        override fun convert(integer: Int): String {
            println("additional logic")
            return passedConverter.convert(integer)
        }
    }
}