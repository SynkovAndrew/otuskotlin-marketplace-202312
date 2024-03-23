package com.otus.otuskotlin.lesson

import kotlin.reflect.KProperty
import kotlin.test.Test

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