package com.otus.otuskotlin.lesson

import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertNotNull

class Exercise4Generics {

    @Test
    fun test() {
        assertNotNull(CovariantImpl().give())
    }
}

interface Invariant<T> {
    fun process(t: T): T
}

interface Covariant<out T> {
    fun give(): T
}

interface Contravariant<in T> {
    fun take(t: T)
}

class InvariantImpl : Invariant<String> {
    override fun process(t: String): String {
        println(t)
        return t + UUID.randomUUID().toString()
    }
}

class CovariantImpl : Covariant<String> {
    override fun give(): String {
        return UUID.randomUUID().toString()
    }
}

class ContravariantImpl : Contravariant<String> {
    override fun take(t: String) {
        println(t)
    }
}