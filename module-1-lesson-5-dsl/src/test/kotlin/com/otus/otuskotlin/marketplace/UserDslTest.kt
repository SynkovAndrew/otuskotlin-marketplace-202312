package com.otus.otuskotlin.marketplace

import com.otus.otuskotlin.marketplace.dsl.user
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class UserDslTest {

    @Test
    fun userTest() {
        val user = user {
            name {
                first =  "Ivan"
                last =  "Ovan"
            }
            contacts {
                phone = "+79163473355"
                email = "aaa@gmail.com"
            }
            actions {
                add(Action.ADD)
                add(Action.CREATE)
            }
        }

        assertEquals("Ivan", user.firstName)
        assertEquals("Ovan", user.lastName)
        assertEquals("+79163473355", user.phone)
        assertEquals("aaa@gmail.com", user.email)
        assertContains(user.actions, Action.ADD)
        assertContains(user.actions, Action.CREATE)
    }
}