package com.otus.otuskotlin.marketplace.hw.easy

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class HWEasy {

    @Test
    fun easyHw() {
        val numbers = generateNumbers()
        val toFind = 10
        val toFindOther = 1000

        val foundNumbers: List<Deferred<Int>> =
            listOf(
                CoroutineScope(Dispatchers.IO).async {
                    findNumberInList(toFind, numbers)
                },
                CoroutineScope(Dispatchers.IO).async {
                    findNumberInList(toFindOther, numbers)
                }
            )


        runBlocking {
            foundNumbers.awaitAll().forEach {
                if (it != -1) {
                    println("Your number $it found!")
                } else {
                    println("Not found number $toFind || $toFindOther")
                }
            }
        }
    }

    @Test
    fun gaming() {
        val numbers = (1..10000).toList()

        val first: Deferred<Int> = CoroutineScope(Dispatchers.IO).async {
            findNumber(105, numbers)
        }

        val second: Deferred<Int> = CoroutineScope(Dispatchers.IO).async {
            findNumber(605, numbers)
        }

        runBlocking {
            assertEquals(first.await(), 105)
            assertEquals(second.await(), 605)
        }
    }


    private fun findNumber(number: Int, list: List<Int>): Int {
        Thread.sleep(2000)
        return list.find { it == number } ?: -1
    }
}