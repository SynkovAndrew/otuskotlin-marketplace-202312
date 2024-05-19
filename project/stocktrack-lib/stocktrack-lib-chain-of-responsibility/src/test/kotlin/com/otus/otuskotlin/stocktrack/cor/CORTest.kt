package com.otus.otuskotlin.stocktrack.cor

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CORTest {

    @Test
    fun test() {
        runTest {
            val chain = chainBuilder<TestContext> {
                processor {
                    name = "ignored"
                    invokeOn { false }
                    process {
                        it.copy(result = "false")
                    }
                }
                processor {
                    name = "with exception"
                    invokeOn { true }
                    process {
                        throw RuntimeException("error")
                    }
                    handleException { e, _ -> println(e) }
                }
                processor {
                    name = "set result"
                    invokeOn { true }
                    process {
                        it.copy(result = it.input)
                    }
                }
            }.build()

            val context = TestContext(input = "arg", "")
            val result = chain.execute(context)

            assertEquals(TestContext(input = "arg", "arg"), result)
        }
    }
}

data class TestContext(
    val input: String,
    val result: String
)

