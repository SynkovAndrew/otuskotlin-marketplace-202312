package com.otus.otuskotlin.stocktrack.cor

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CORTest {

    @Test
    fun test() {
        runTest {
            val chain : Chain<TestContext> = chainBuilder {
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
                    handleException { e, context ->
                        println(e)
                        context
                    }
                }
                processor {
                    name = "set result"
                    invokeOn { true }
                    process {
                        it.copy(result = it.input)
                    }
                }
                chain {
                    invokeOn { false }
                    processor {
                        name = "add result"
                        invokeOn { true }
                        process {
                            it.copy(result = "${it.result} 1111")
                        }
                    }
                }
                chain {
                    invokeOn { true }
                    processor {
                        name = "add result"
                        invokeOn { true }
                        process {
                            it.copy(result = "${it.result} 3333")
                        }
                    }
                }
            }

            val context = TestContext(input = "arg", "")
            val result = chain.execute(context)

            assertEquals(TestContext(input = "arg", "arg 3333"), result)
        }
    }
}

data class TestContext(
    val input: String,
    val result: String
)

