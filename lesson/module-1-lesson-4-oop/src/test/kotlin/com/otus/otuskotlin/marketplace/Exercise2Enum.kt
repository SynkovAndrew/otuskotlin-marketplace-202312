package com.otus.otuskotlin.marketplace

import kotlin.test.Test
import kotlin.test.assertEquals


class Exercise2Enum {

    @Test
    fun minus() {
        val res = mutableListOf<String>()
        SomeKindOfEnum.FOO.iterator().forEach {
            res.add(it.doIt())
        }
        assertEquals(listOf("AAAA", "BBB"), res)
    }


    enum class SomeKindOfEnum: Iterable<SomeKindOfEnum> {
        FOO {
            override fun doIt(): String {
                return "AAAA"
            }
        },
        BAR {
            override fun doIt(): String {
                return "BBB"
            }
        };


        abstract fun doIt(): String

        override fun iterator(): Iterator<SomeKindOfEnum> {
            return listOf(FOO, BAR).iterator()
        }
    }

}