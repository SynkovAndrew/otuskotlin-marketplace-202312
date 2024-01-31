package com.otus.otuskotlin.marketplace

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.test.Test

class Exercise3Delegate {
    @Test
    fun test() {
        val example = DelegateExample()

        println(example.constVal)
        assertEquals(example.constVal, 100501)

        println(example.lazyVal)
        assertEquals(example.lazyVal, 42)
    }
}

private class ConstValue(private val value: Int) : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        TODO("Not yet implemented")
    }
}

private class DelegateExample {
    val constVal by ConstValue(100501)
    val lazyVal by lazy {
        println("calculate...")
        42
    }
}