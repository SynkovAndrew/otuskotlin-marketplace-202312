package com.otus.otuskotlin.stocktrack

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.use
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class SocketLoggerWrapper(
    override val loggerId: String,
    private val host: String = "127.0.0.1",
    private val port: Int = 9002,
    private val emitToStdout: Boolean = true,
    bufferSize: Int = 16,
    overflowPolicy: BufferOverflow = BufferOverflow.SUSPEND,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default + CoroutineName("Logging")),
) : LoggerWrapper {
    private val selectorManager = SelectorManager(Dispatchers.IO)
    private val sharedFlow: MutableSharedFlow<LogData> = MutableSharedFlow(
        extraBufferCapacity = bufferSize,
        onBufferOverflow = overflowPolicy
    )
    private val isActive: AtomicBoolean = atomic(true)
    private val jsonSerializer: Json = Json { encodeDefaults = true }
    private val job: Job = scope.launch { handleLogs() }

    val isReady: AtomicBoolean = atomic(false)

    override fun log(
        level: LogLevel,
        message: String,
        throwable: Throwable?,
        data: Any?,
        arguments: Map<String, Any>,
        marker: String
    ) {
        runBlocking {
            sharedFlow.emit(
                LogData(
                    level = level,
                    message = message,
                )
            )
        }
    }

    override fun close() {
        isActive.value = false
        isReady.value = false
        job.cancel(message = "canceled")
    }

    private suspend fun handleLogs() {
        while (isActive.value) {
            try {
                aSocket(selectorManager).tcp()
                    .connect(host, port)
                    .use { socket ->
                        socket.openWriteChannel().use {
                            sharedFlow
                                .onSubscription { isReady.value = true }
                                .collect {
                                    val json = jsonSerializer.encodeToString(LogData.serializer(), it)
                                    if (emitToStdout) {
                                        println(json)
                                    }
                                    writeStringUtf8(json + "\n")
                                    flush()
                                }
                        }
                    }
            } catch (e: Throwable) {
                println("Error connecting log socket: $e")
                e.printStackTrace()
                delay(300)
            }
        }
    }
}