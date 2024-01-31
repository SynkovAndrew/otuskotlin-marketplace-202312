package com.otus.otuskotlin.marketplace

import kotlin.reflect.KProperty
import kotlin.test.Test
import kotlin.test.assertEquals

class DelegatePropsTest {

    @Test
    fun test() {
        val e = Example()
        e.p
    }

    class Example {
        var p: String by Delegate()
    }

    class Delegate {
        operator fun getValue(example: Example, property: KProperty<*>): String {
            return "aaa"
        }

        operator fun setValue(example: Example, property: KProperty<*>, s: String) {
            TODO("Not yet implemented")
        }
    }
}