package com.otus.otuskotlin.marketplace

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertContains

class Exercise3Generic {
    @Test
    fun genericTest() {
        assertEquals("String", variant2<String>())
    }

    @Test
    fun elementAsListTest() {
        assertContains(elementAsList(12), 12)
    }

    @Test
    fun testReified() {
        willNotCompile(5)
    }

    private inline fun <reified T> willNotCompile(variable: T) {
        println(T::class.java)
    }

    fun variant1(klass: KClass<*>): String = klass.simpleName ?: "(unknown)"

    inline fun <reified T> variant2() = variant1(T::class)

    fun <T> elementAsList(el: T): List<T> = listOf(el)
}