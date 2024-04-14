package com.otus.otuskotlin.stocktrack

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds
import kotlin.use

class SocketLoggerTest {

    @Test
    fun socketTest() {
        runTest(timeout = 3.seconds) {
            val port = Random.nextInt(9000, 12000)
            val loggerSettings = SocketLoggerSettings(port = port)

            withContext(Dispatchers.Default) {
                val selectorManager = SelectorManager(Dispatchers.IO)
                val serverJob = launch {
                    aSocket(selectorManager).tcp()
                        .bind("127.0.0.1", port)
                        .use { serverSocket ->
                            serverSocket.accept()
                                .use { socket ->
                                    flow {
                                        val receiveChannel = socket.openReadChannel()
                                        while (true) {
                                            receiveChannel.readUTF8Line(8 * 1024)?.let { emit(it) }
                                        }
                                    }.take(100)
                                        .collect {
                                            println("GOT: $it")
                                        }
                                }
                        }
                }
                // Prepare Logger
                socketLoggerWrapper("test", loggerSettings)
                    .use { logger ->
                        // Wait for logger is ready
                        while ((logger as? SocketLoggerWrapper)?.isReady?.value != true) {
                            delay(1)
                        }
                        coroutineScope {
                            launch {
                                repeat(100) {
                                    logger.info(
                                        message = "Test message $it",
                                        data = object {
                                            val str: String = "one"
                                            val iVal: Int = 2
                                        }
                                    )
                                }
                            }
                        }
                    }
                serverJob.cancelAndJoin()
            }
        }
    }
}
