package com.otus.otuskotlin.lesson

import java.math.BigDecimal
import java.util.Currency
import kotlin.test.Test
import kotlin.test.assertEquals

val RUBLE: Currency = Currency.getInstance("RUR")

class Exercise1Operator {

    @Test
    fun minus() {
        val cash1 = Cash(BigDecimal.valueOf(150.5), RUBLE)
        val cash2 = Cash(BigDecimal.valueOf(50.3), RUBLE)

        assertEquals(
            Cash(BigDecimal.valueOf(100.2), RUBLE),
            cash1 - cash2
        )
    }


    data class Cash(
        private val amount: BigDecimal,
        private val currency: Currency
    ) {
        operator fun minus(other: Cash): Cash {
            require(other.currency == this.currency)

            return Cash(
                amount = amount.minus(other.amount),
                currency = currency
            )
        }

        companion object {
            val NONE = Cash(
                amount = BigDecimal.ZERO,
                currency = RUBLE
            )
        }
    }

}