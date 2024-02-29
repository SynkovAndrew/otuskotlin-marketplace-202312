package com.otus.otuskotlin.marketplace

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

class UserTest {

    @Test
    fun test() {
        val user = User("1", "Ivan", 24)
        assertEquals("1", user.id)
        assertEquals("Ivan", user.name)
        assertEquals(24, user.age)
    }

    @Test
    fun firstKmp() {
        val user = User("1", "Ivan", 24)
        val service = UserService()
        val res = service.serve(user)
        assertTrue(setOf("JVM", "JS", "Native").any { res.contains(it) })
    }

    @Test
    fun coroutine() {
        runTest {
            val user = User("1", "Ivan", 24)
            val service = UserAsyncService()
            val res = service.serve(user)
            assertEquals(res.second, user)
            assertTrue(setOf("JVM", "JS", "Native").any { res.first.contains(it) })
        }
    }

    @Test
    fun longCoroutines() {
        runTest {
            measureTime {
                val user = User("1", "Ivan", 24)
                val service = UserLongService()
                val res = service.serve(user)
                assertEquals(res.second, user)
                assertTrue(setOf("JVM", "JS", "Native").any { res.first.contains(it) })
            }.also { println("GENERAL TIME: $it") }
        }
    }

    @Test
    fun realTime() {
        runTest(timeout = 10.seconds) {
            withContext(Dispatchers.Default) {
                measureTime {
                    val user = User("1", "Ivan", 24)
                    val service = UserLongService()
                    val res = service.serve(user)
                    assertEquals(res.second, user)
                    assertTrue(setOf("JVM", "JS", "Native").any { res.first.contains(it) })
                }.also { println("GENERAL TIME: $it") }
            }
        }
    }
}